package com.uniits.carlink.dao.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.uniits.carlink.dao.IAppStoreDAO;
import com.uniits.carlink.pojo.AppBaseInfo;
import com.uniits.carlink.pojo.AppDetailInfo;

@Component(value = "appStoreDAO")
@Scope(value = "prototype")
public class AppStoreDAOImpl implements IAppStoreDAO {

	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@Resource(name = "hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public long addAppInfo(AppBaseInfo appBaseInfo) throws Exception {
		hibernateTemplate.save(appBaseInfo);
		return appBaseInfo.getAppid();
	}
	
	public List<AppBaseInfo> queryAppList(final int page ,final int num ,final int category) throws Exception{
		Object obj = hibernateTemplate.execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Object obj = null;
				Query query = null;
				if(category == -1) {
					query = session.createQuery("from AppBaseInfo info where info.priorLevel<>-1 order by info.priorLevel desc");
					
				}else {
					query = session.createQuery("from AppBaseInfo info where info.priorLevel<>-1 and bitand(info.category , :category) <> 0 order by  info.priorLevel desc");
					query.setInteger("category", category);
				}
				query.setFirstResult(page * num);
				query.setMaxResults(num);
				obj = query.list();
				return obj;
			}
		});
		return (List<AppBaseInfo>) obj;
	}
	
	public List<AppBaseInfo> queryAppListByKeyword(final String keyword , final int page ,final int num ,final int category) throws Exception {
		Object obj = hibernateTemplate.execute(new HibernateCallback<Object>(){
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Object obj = null;
				Query query = null;
				if(category == -1) {
					query = session.createQuery("from AppBaseInfo info where info.priorLevel<>-1 and info.name like :keyword order by info.priorLevel desc");
					query.setString("keyword", keyword);
					
				}else {
					query = session.createQuery("from AppBaseInfo info where info.priorLevel<>-1 and bitand(info.category , :category) <> 0 and info.name like :keyword order by  info.priorLevel desc");
					query.setInteger("category", category);
					query.setString("keyword", keyword);
				}
				query.setFirstResult(page * num);
				query.setMaxResults(num);
				obj = query.list();
				return obj;
			}
		});
		return (List<AppBaseInfo>) obj;
	}
	
	public AppDetailInfo queryAppDetailInfo(long appid)throws Exception {
		AppDetailInfo appDetailInfo = hibernateTemplate.get(AppDetailInfo.class, appid);
		return appDetailInfo;
	}
	
	
	public AppBaseInfo queryAppBaseInfo(long appid)throws Exception {
		AppBaseInfo appBaseInfo = hibernateTemplate.get(AppBaseInfo.class, appid);
		return appBaseInfo;
	}
	
	public long updateLoadCount(final long appid) throws Exception{
		Object obj = hibernateTemplate.execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "UPDATE T_APPBASEINFO  SET DOWNLOADCOUNT= (SELECT DOWNLOADCOUNT FROM T_APPBASEINFO WHERE APPID=:appid) + 1 where APPID=:appid";
				SQLQuery sqlQuery = session.createSQLQuery(sql);
				sqlQuery.setLong("appid",appid);
				Object obj = sqlQuery.executeUpdate();
				return obj;
			}
		});
		return Long.parseLong(obj.toString());
	}
	
	public long getAppNextId() throws Exception {
		Object obj = hibernateTemplate.execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("SELECT SEQ_APPINFO_ID.NEXTVAL FROM DUAL");
				Object obj = sqlQuery.uniqueResult();
				return obj;
			}
		});
		return Long.parseLong(obj.toString());
	}

}
