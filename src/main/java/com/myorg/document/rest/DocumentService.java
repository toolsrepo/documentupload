package com.myorg.document.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.myorg.document.models.Document;
import com.myorg.document.models.DocumentDao;
import com.myorg.document.models.DocumentPayLoad;
import com.myorg.document.models.LoanDocument;
import com.myorg.document.models.LoanDocumentDao;
import com.myorg.document.models.LoanDocumentPK;
import com.myorg.document.upload.BoxUpload;

@Component
@Path("/document")
public class DocumentService {

	@Autowired
	private Environment env;

	@Autowired
	private DocumentDao _documentDao;
	
	@Autowired
	private LoanDocumentDao _loanDocumentDao;
	
	@Autowired
	private BoxUpload _boxUpload;

	@GET
	@Path("/documentlist")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocumentList() {
		GenericEntity<List<Document>> entity;
		List<Document> documents = _documentDao.getAll();
		System.out.println("List of documents :" + documents);
		entity = new GenericEntity<List<Document>>(documents) {};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("/retrieveDocumentList/{mortgageApplicationID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDocumentList(@PathParam("mortgageApplicationID") Long mortgageApplicationID) {
		GenericEntity<List<DocumentPayLoad>> entity;
		List<LoanDocument> documents = _loanDocumentDao.getByLoanId(mortgageApplicationID);
		System.out.println("For loanId ["+mortgageApplicationID+"] List of documents by loan Id:" + documents);
		List<DocumentPayLoad> uploadedDocuments = new ArrayList<DocumentPayLoad>();
		if (documents != null && documents.size() > 0) {
			for (Iterator<LoanDocument> iterator = documents.iterator(); iterator.hasNext();) {
				LoanDocument document = (LoanDocument) iterator.next();
				DocumentPayLoad payLoad = new DocumentPayLoad();
				payLoad.setMortgageApplicationID(document.getLoanDocumentComposite().getMortgageApplicationID());
				payLoad.setDocumentTypeId(document.getLoanDocumentComposite().getDocumentTypeId());
				payLoad.setDocumentDescription(document.getDocumentMetadata().getDocumentDescription());
				payLoad.setDocumentDownloadLink("/document/downloadDocument/"+mortgageApplicationID+"/"+document.getLoanDocumentComposite().getDocumentTypeId());
				uploadedDocuments.add(payLoad);
			}
		}
		entity = new GenericEntity<List<DocumentPayLoad>>(uploadedDocuments) {};
		return Response.ok(entity).build();
	}
	
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@FormDataParam("documentPayload") InputStream uploadedInputStream,
			@FormDataParam("documentPayload") FormDataContentDisposition fileDetail,
			@FormDataParam("mortgageApplicationID") Long mortgageApplicationID,
			@FormDataParam("documentTypeId") Integer documentTypeId) throws Throwable {
		String filepath = null;
		System.out.println("mortgageApplicationID="+mortgageApplicationID+", documentTypeId="+documentTypeId);
		// Get the filename and build the local file path
		Document documentMetaData = _documentDao.getDocumentDetails(documentTypeId);
		String filename = fileDetail.getFileName();
		if (documentMetaData != null) {
			String extension = filename.substring(filename.lastIndexOf("."), filename.length());
			System.out.println("Extension ="+extension);
			filename = documentMetaData.getDocumentName() + extension;
		}

		LoanDocumentPK loanDocumentComposite = new LoanDocumentPK();
		loanDocumentComposite.setMortgageApplicationID(mortgageApplicationID);
		loanDocumentComposite.setDocumentTypeId(documentTypeId);
		loanDocumentComposite.setSequenceNumber(1);
		
		LoanDocument loanDocument = new LoanDocument();
		loanDocument.setLoanDocumentComposite(loanDocumentComposite);
		byte[] original = IOUtils.toByteArray(uploadedInputStream);
		byte[] copy = original.clone();
		loanDocument.setDocumentPayload(original);
		loanDocument.setDocumentName(filename);
		
		boolean boxUploaded = false;
		try {
			String downloadURL = _boxUpload.upload(mortgageApplicationID, copy, filename);
			loanDocument.setDocumentPath(downloadURL);
			boxUploaded = true;
			filepath = loanDocument.getDocumentPath();
		} catch (Exception t) {
			System.err.println("Unable to upload to BOX. Cause :"+t.getMessage()+"\n. It will be saved in file system location");
			t.printStackTrace();
		}
		
		if(!boxUploaded) {
			// save in file system
			String directoryPath = env.getProperty("mortgage.paths.uploadedFiles") + File.separator + mortgageApplicationID + File.separator;
			File directory = new File(directoryPath);
			if (!directory.exists()) directory.mkdirs();
			filepath = Paths.get(directoryPath, filename).toString();

			upload(copy, filepath);
			loanDocument.setDocumentPath(filepath);
		}
		String output = "File uploaded to : " + filepath;
		System.out.println(output);

		_loanDocumentDao.saveDocument(loanDocument);

		System.out.println("Uploaded Successfully.");
		return Response.status(200).entity(output).build();
	}
	
	@GET
	@Path("/downloadDocument/{mortgageApplicationID}/{documentTypeId}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadDocument(@PathParam("mortgageApplicationID") final long mortgageApplicationID, final @PathParam("documentTypeId") long documentTypeId) {
		 final LoanDocument loanDocument = _loanDocumentDao.getDocument(mortgageApplicationID, documentTypeId, 1);;
		 StreamingOutput stream =  new StreamingOutput() {
			@Override
			public void write(OutputStream arg0) throws IOException{
				BufferedOutputStream bus = new BufferedOutputStream(arg0);
				try {
					byte[] bufferPayLoad = loanDocument.getDocumentPayload();
					bus.write(bufferPayLoad);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition","attachment; filename ="+ loanDocument.getDocumentName()).build();
	}
	
	@POST
	@Path("/deleteDocument/{mortgageApplicationID}/{documentTypeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDocument(@PathParam("mortgageApplicationID") final long mortgageApplicationID, final @PathParam("documentTypeId") long documentTypeId) {
		 final LoanDocument loanDocument = _loanDocumentDao.getDocument(mortgageApplicationID, documentTypeId, 1);;
		 _loanDocumentDao.deleteDocument(loanDocument);
		return Response.status(200).entity("Deleted Successfully.").build();
	}
	
	
	private void upload(byte[] byteArray, String uploadedFileLocation) {
		try {
			FileUtils.writeByteArrayToFile(new File(uploadedFileLocation), byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}