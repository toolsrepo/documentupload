package com.myorg.document.models;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class LoanDocumentDao {

	@Autowired
	private SessionFactory _sessionFactory;

	private Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<LoanDocument> getByLoanId(long mortgageApplicationID) {
		Query query = getSession().createQuery("from LoanDocument where loanDocumentComposite.mortgageApplicationID = :loanId and "
				+ "documentMetadata.dataTypeId = :dataTypeId");
		query.setParameter("loanId", mortgageApplicationID);
		query.setParameter("dataTypeId", Document.DATATYPE_ID);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public LoanDocument getDocument(long mortgageApplicationID, long documentTypeId, long sequenceNumber) {
		Query query = getSession().createQuery("from LoanDocument where "
				+ "loanDocumentComposite.mortgageApplicationID = :loanId and "
				+ "loanDocumentComposite.documentTypeId = :documentTypeId and "
				+ "loanDocumentComposite.sequenceNumber = :sequenceNumber");
		query.setParameter("loanId", mortgageApplicationID);
		query.setParameter("documentTypeId", documentTypeId);
		query.setParameter("sequenceNumber", sequenceNumber);
		List<LoanDocument> list = query.list();
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public void saveDocument(LoanDocument loanDocument) {
		getSession().save(loanDocument);
	}
	
	public void deleteDocument(LoanDocument loanDocument) {
		getSession().delete(loanDocument);
	}
}
