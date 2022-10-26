package Tmall_Bean;

import java.util.List;

public class Category {
    private String name;
    private int id;
    List<Product> products;
    List<List<Product>> productByRow;
    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<List<Product>> getProductByRow() {
        return productByRow;
    }

    public void setProductByRow(List<List<Product>> productByRow) {
        this.productByRow = productByRow;
    }

    @Override
    public String toString() {
        return "Category [name=" + name + "]";

    }
}
