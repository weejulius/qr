package demo.m.qr1688.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jyu on 15-3-5.
 */
public class Spu implements Serializable {

    public String subject;
    public String price;
    public String offerId;
    public String spuId;
    public List<KV> props;
    public String discountPrice;
    public String isBook;
    public String img;

    public Spu(String subject, String price) {
        this.subject = subject;
        this.price = price;
    }
}
