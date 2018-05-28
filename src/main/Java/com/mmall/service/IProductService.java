package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by Administrator on 2018/5/22.
 */
public interface IProductService {
    //增加或者更新产品
    ServerResponse saveOrUpdateProduct(Product product);

    //获取商品的销售状态
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    //获取商品的详细信息
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    //获取商品列表
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    //获取搜索结果
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    //前台查询商品详情
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    //前台根据关键字和category查询
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
