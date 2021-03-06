package action;


import com.google.gson.Gson;
import dao.ImgDao;
import dao.JedisClient;
import dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import pojo.Img;
import pojo.Product;
import service.ProductService;
import utils.FastDFSClient;
import utils.FileUtils;
import utils.ResponseResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Auther: blxf
 * @Date: 2019-08-05 15:22
 * @Description:
 */
@Controller
@Scope("prototype")
public class ProductAction extends BaseAction{

    @Autowired
    private ProductService productService;
    @Autowired
    private ImgDao imgDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private JedisClient jedisClient;
    private Product product;
    private Img img;
    private File imgFile;
    private String imgFileContentType;
    private String imgFileFileName;
    private int startPage;
    private int item;
    private int id;
    private static String KEY_GETPRODUCTS = "getProducts";
    private static String KEY_GETNEWPRODUCTS = "getNewProducts";
    private static String KEY_GETPRODUCTINFO = "getProductInfo";
    private static String KEY_GETPROBYCATE = "getProByCate";

    public String  getProducts() throws IOException {
        try {
            String redisResult = jedisClient.hget(KEY_GETPRODUCTS,"startPage="+startPage+"&item="+item);
            if (redisResult != null){
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(redisResult);
                return NONE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = productService.showProducts(startPage , item);
        jedisClient.hset(KEY_GETPRODUCTS,"startPage="+startPage+"&item="+item,result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result);
        return NONE;
    }

    public String addProduct() throws FileNotFoundException {
        try {
            FastDFSClient fastDFSClient = new FastDFSClient();
            String fileName = fastDFSClient.uploadFile(FileUtils.File2byte(imgFile));
            img.setImg_addr(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        productService.addProduct(product,img);
        jedisClient.del(KEY_GETPRODUCTS);
        jedisClient.del(KEY_GETNEWPRODUCTS);
        jedisClient.del(KEY_GETPROBYCATE);
        return PRODUCT;
    }
    public String updateProduct() throws FileNotFoundException,IOException{
        if (imgFile == null){
            System.out.println("没有图片更新");
            System.out.println(product);
            try {
                String result = productService.updateProduct(product);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(result);
                jedisClient.del(KEY_GETPRODUCTS);
                jedisClient.del(KEY_GETNEWPRODUCTS);
                jedisClient.hdel(KEY_GETPRODUCTINFO,"pro_id="+id);
                jedisClient.del(KEY_GETPROBYCATE);
                return PRODUCT;
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(new Gson().toJson(ResponseResult.build(500,"修改发生错误")));
            return NONE;
        }else {



            try {
                FastDFSClient fastDFSClient = new FastDFSClient();
                String fileName = fastDFSClient.uploadFile(FileUtils.File2byte(imgFile));
                img.setImg_addr(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }


            String fileName="";
            try {
                FastDFSClient fastDFSClient = new FastDFSClient();
                 fileName = fastDFSClient.uploadFile(FileUtils.File2byte(imgFile));
                img.setImg_addr(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }


            productService.updateProduct(product);
            Product p = productDao.getProduct(this.product.getPro_id());
            Img img = imgDao.getImg(p.getPro_imgId());
            img.setImg_addr(fileName);
            imgDao.updateImg(img);
            jedisClient.del(KEY_GETPRODUCTS);
            jedisClient.del(KEY_GETNEWPRODUCTS);
            jedisClient.hdel(KEY_GETPRODUCTINFO,"pro_id="+id);
            jedisClient.del(KEY_GETPROBYCATE);
            return PRODUCT;
        }
    }

    public String getProductInfo() throws IOException{
        if (id == 0){
            return NONE;
        }
        try {
            String redisResult = jedisClient.hget(KEY_GETPRODUCTINFO,"pro_id="+id);
            if (redisResult != null){
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(redisResult);
                return NONE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = productService.getProduct(id);
        jedisClient.hset(KEY_GETPRODUCTINFO,"pro_id="+id,result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result);
        return NONE;
    }

    public String delProduct(){

        return PRODUCT;
    }

    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        return format;
    }


    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public String getImgFileContentType() {
        return imgFileContentType;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }

    public String getImgFileFileName() {
        return imgFileFileName;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
