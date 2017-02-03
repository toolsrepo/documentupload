package com.myorg.document.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mortgage.transaction_data_item")
public class LoanDocument implements Serializable{
	
	private static final long serialVersionUID = -6713284053396009032L;

	@EmbeddedId
	private LoanDocumentPK loanDocumentComposite;
	
	@Column(name ="COL_LARGE_BIN_VAL")
	private byte[] documentPayload;
	
	
	@Column(name ="COL_SIMPLE_VAL")
	private String documentName;
	
	@Lob
	@Column(name = "COL_LARGE_VAL")
	private String documentPath;
	
	@ManyToOne
	@JoinColumn(name="COL_ID", referencedColumnName="ATTRIBUTE_ID", insertable=false, updatable=false)
	private Document documentMetadata;

	public LoanDocumentPK getLoanDocumentComposite() {
		return loanDocumentComposite;
	}

	public void setLoanDocumentComposite(LoanDocumentPK loanDocumentComposite) {
		this.loanDocumentComposite = loanDocumentComposite;
	}

	public byte[] getDocumentPayload() {
		return documentPayload;
	}

	public void setDocumentPayload(byte[] documentPayload) {
		this.documentPayload = documentPayload;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Document getDocumentMetadata() {
		return documentMetadata;
	}

	public void setDocumentMetadata(Document documentMetadata) {
		this.documentMetadata = documentMetadata;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
}
