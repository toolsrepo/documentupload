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
public class DocumentDao {

	
	@Autowired
	private SessionFactory _sessionFactory;

	private Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<Document> getAll() {
		return getSession().createQuery("from Document where dataTypeId = :dataTypeId").setParameter("dataTypeId", Document.DATATYPE_ID).list();
	}
	
	@SuppressWarnings("unchecked")
	public Document getDocumentDetails(int documentTypeId) {
		Query query = getSession().createQuery("from Document where dataTypeId = :dataTypeId and documentTypeId = :documentTypeId");
		query.setParameter("dataTypeId", Document.DATATYPE_ID);
		query.setParameter("documentTypeId", documentTypeId);
		List<Document> list = query.list();
		if(list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
