package com.myorg.document.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mortgage.attribute_metadata")
public class Document implements Serializable{

	private static final long serialVersionUID = -2474587140322632122L;

	public static final String DATATYPE_ID = "2"; 

	@Id
	@Column(name ="ATTRIBUTE_ID")
	private int documentTypeId;

	@Column(name ="COL_NM")
	@JsonIgnore
	private String documentName;
	
	@Column(name ="COL_DESC")
	private String documentDescription;
	
	@Column(name ="DATATYPE_ID")
	@JsonIgnore
	private String dataTypeId;
	
	public Document() {}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getDataTypeId() {
		return dataTypeId;
	}

	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}

}
