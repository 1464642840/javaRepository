package dao.Impl;

import dao.ImgDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pojo.Img;

import java.io.Serializable;

/**
 * @Auther: blxf
 * @Date: 2019-08-05 15:27
 * @Description:图片dao的实现类
 */
@Repository
@Transactional
public class ImgDaoImpl extends HibernateDaoSupport implements ImgDao {
    @Autowired
    public void setSF(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }


    public int addImg(Img img) {
        Serializable id = this.getHibernateTemplate().save(img);
        return  Integer.parseInt(id.toString());
    }


    @Transactional
    public void updateImg(Img img) {
        this.getHibernateTemplate().update(img);
    }


    public Img getImg(int id) {
        Img img = this.getHibernateTemplate().get(Img.class,id);
        return img;
    }


    public String getImgUrl(int id) {
        Img img = this.getHibernateTemplate().get(Img.class , id);
        return img.getImg_addr();
    }
}
