package com.myorg.document.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LoanDocumentPK implements Serializable {
	private static final long serialVersionUID = -7774385743993478131L;
	
	@Column(name ="TRANSACTION_ID")
	private long mortgageApplicationID;
	
	@Column(name ="COL_ID")
	private long documentTypeId;
	
	@Column(name ="SEQUENCE_NO")
	private long sequenceNumber;
	
	public LoanDocumentPK(){}
	
	public LoanDocumentPK(long mortgageApplicationID, long documentTypeId, long sequenceNumber){
		this.mortgageApplicationID = mortgageApplicationID;
		this.documentTypeId = documentTypeId;
		this.sequenceNumber = sequenceNumber;
	}

	public long getMortgageApplicationID() {
		return mortgageApplicationID;
	}

	public void setMortgageApplicationID(long mortgageApplicationID) {
		this.mortgageApplicationID = mortgageApplicationID;
	}

	public long getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(long documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	
}
