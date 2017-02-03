package com.myorg.document.upload;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFile;
import com.box.sdk.BoxFolder;
import com.box.sdk.FileUploadParams;
import com.myorg.document.config.BoxConnection;

@Component
public class BoxUpload {

	@Autowired
	private BoxConnection _boxConnection;
	
	public String upload(long mortgageApplicationID, byte[] documentStream, String documentName) throws Throwable{
		String downloadURL = null;
		BoxAPIConnection apiConnection = _boxConnection.getAPIConnectionByDeveloperToken();
		BoxFolder mortgageFolder = BoxConnection.getFolder(BoxFolder.getRootFolder(apiConnection), BoxConnection.BASE_MAX_DEPTH, _boxConnection.getBoxMortgageBasePath());
		System.out.println("mortgageFolder info="+mortgageFolder);
		if (mortgageFolder  != null) {
			BoxFolder loanFolder = BoxConnection.getFolder(mortgageFolder , BoxConnection.BASE_MAX_DEPTH, mortgageApplicationID+"");
			if(loanFolder == null) {
				loanFolder = mortgageFolder.createFolder(mortgageApplicationID+"").getResource();
			}
			FileUploadParams fup = new FileUploadParams();
			fup.setContent(new ByteArrayInputStream(documentStream));
			fup.setName(documentName);
			BoxFile.Info newFileInfo = loanFolder.uploadFile(fup);
			downloadURL = newFileInfo.getResource().getDownloadURL().toString();
			System.out.println("Uploaded file in BOX :"+newFileInfo.getName()+", against loanId:"+loanFolder.getInfo().getName()+""
					+ "\n And downloadURL is :\n"+downloadURL);
		}
		return downloadURL;
	}
}
