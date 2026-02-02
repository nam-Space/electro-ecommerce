package com.electro.product;

import com.electro.constant.ResourceName;
import com.electro.constant.SearchFields;
import com.electro.controller.GenericController;
import com.electro.dto.ListResponse;
import com.electro.dto.general.ImageRequest;
import com.electro.dto.general.ImageResponse;
import com.electro.dto.inventory.DocketResponse;
import com.electro.dto.product.*;
import com.electro.entity.general.Image;
import com.electro.entity.product.*;
import com.electro.mapper.product.ProductMapper;
import com.electro.repository.product.ProductRepository;
import com.electro.service.CrudService;
import com.electro.service.GenericService;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductRepositoryTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private GenericService<Product, ProductRequest, ProductResponse> productService;

    @Autowired
    private GenericController<ProductRequest, ProductResponse> productController;

    @BeforeEach
    void initData() {
        productController.setCrudService(productService.init(
                context.getBean(ProductRepository.class),
                context.getBean(ProductMapper.class),
                SearchFields.PRODUCT,
                ResourceName.PRODUCT
        ));

        productController.setRequestType(ProductRequest.class);
    }

    @Test
    @Transactional
    public void testGetAllProduct_SP001() throws Exception {
        int size = 5;
        ResponseEntity<ListResponse<ProductResponse>> res = productController.getAllResources(1, size, null, null, null, false);
        List<ProductResponse> products = res.getBody().getContent();
        assertThat(products.size()).isEqualTo(size);
        System.out.println("Expected size: " + size);
        System.out.println("Actual size: " + products.size());
    }

    @Test
    @Transactional
    public void testGetAllProduct_SP002() throws Exception {
        int size = 0;
        try {
            ResponseEntity<ListResponse<ProductResponse>> res = productController.getAllResources(1, size, null, null, null, false);
            List<ProductResponse> products = res.getBody().getContent();
            assertThat(products.size()).isEqualTo(size);
            System.out.println("Expected size: " + size);
            System.out.println("Actual size: " + products.size());
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Danh sách sản phẩm rỗng");
        }
    }

    @Test
    @Transactional
    public void testGetAllProduct_SP003() throws Exception {
        int size = -1;
        try {
            ResponseEntity<ListResponse<ProductResponse>> res = productController.getAllResources(1, size, null, null, null, false);
            List<ProductResponse> products = res.getBody().getContent();
            assertThat(products.size()).isEqualTo(size);
            System.out.println("Expected size: " + size);
            System.out.println("Actual size: " + products.size());
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Danh sách sản phẩm rỗng");
        }
    }

    @Test
    @Transactional
    public void testGetProductById_SP004() throws Exception {
        ResponseEntity<ProductResponse> res = productController.getResource(1L);
        ProductResponse product = res.getBody();
        assertThat(product).isNotNull();
        System.out.println("Product " + product);
    }

    @Test
    @Transactional
    public void testGetProductById_SP005() throws Exception {
        Long id = 999L;
        try {
            ResponseEntity<ProductResponse> res = productController.getResource(id);
            ProductResponse product = res.getBody();

            assertThat(product).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Sản phẩm có id không tồn tại");
        }

    }

    @Test
    public void testCreateProduct_SP006() throws Exception {
        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M1");
            product.setCode("MAC-001");
            product.setSlug("macbook-m1");
            product.setShortDescription("Đây là sản phẩm Macbook M1");
            product.setDescription("Đây là sản phẩm Macbook M1");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNotNull();
            System.out.println("NewProduct: " + newProduct);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }
    }

    @Test
    public void testCreateProduct_SP007() throws Exception {
        String name = null;
        try {
            ProductRequest product = new ProductRequest();
            product.setName(name);
            product.setCode("MAC-002");
            product.setSlug("macbook-m2");
            product.setShortDescription("Đây là sản phẩm Macbook M2");
            product.setDescription("Đây là sản phẩm Macbook M2");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
            System.out.println("NewProduct: " + newProduct);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Tên sản phẩm không được để trống");
        }
    }

    @Test
    public void testCreateProduct_SP008() throws Exception {
        String code = "prod-87";

        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M3");
            product.setCode(code);
            product.setSlug("macbook-m3");
            product.setShortDescription("Đây là sản phẩm Macbook M3");
            product.setDescription("Đây là sản phẩm Macbook M3");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã sản phẩm đã tồn tại");
        }
    }

    @Test
    public void testCreateProduct_SP009() throws Exception {
        String code = null;
        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M4");
            product.setCode(code);
            product.setSlug("macbook-m4");
            product.setShortDescription("Đây là sản phẩm Macbook M4");
            product.setDescription("Đây là sản phẩm Macbook M4");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
            System.out.println("NewProduct: " + newProduct);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã sản phẩm không được để trống");
        }
    }

    @Test
    public void testCreateProduct_SP010() throws Exception {
        String slug = "prod-87";

        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M5");
            product.setCode("MAC-005");
            product.setSlug(slug);
            product.setShortDescription("Đây là sản phẩm Macbook M5");
            product.setDescription("Đây là sản phẩm Macbook M5");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Slug đã tồn tại");
        }
    }

    @Test
    public void testCreateProduct_SP011() throws Exception {
        String slug = null;
        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M6");
            product.setCode("MAC-006");
            product.setSlug(slug);
            product.setShortDescription("Đây là sản phẩm Macbook M6");
            product.setDescription("Đây là sản phẩm Macbook M6");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
            System.out.println("NewProduct: " + newProduct);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Slug không được để trống");
        }
    }

    @Test
    public void testCreateProduct_SP012() throws Exception {
        Integer status = null;
        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M7");
            product.setCode("MAC-007");
            product.setSlug("macbook-m7");
            product.setShortDescription("Đây là sản phẩm Macbook M7");
            product.setDescription("Đây là sản phẩm Macbook M7");
            product.setStatus(status);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
            System.out.println("NewProduct: " + newProduct);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Trạng thái sản phẩm không được để trống");
        }
    }

    @Test
    public void testCreateProduct_SP013() throws Exception {
        Long categoryId = 999L;

        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M8");
            product.setCode("MAC-008");
            product.setSlug("macbook-m8");
            product.setShortDescription("Đây là sản phẩm Macbook M8");
            product.setDescription("Đây là sản phẩm Macbook M8");
            product.setStatus(1);
            product.setCategoryId(categoryId);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Danh mục sản phẩm có id không tồn tại");
        }
    }

    @Test
    public void testCreateProduct_SP014() throws Exception {
        Long brandId = 999L;

        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M9");
            product.setCode("MAC-009");
            product.setSlug("macbook-m9");
            product.setShortDescription("Đây là sản phẩm Macbook M9");
            product.setDescription("Đây là sản phẩm Macbook M9");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(brandId);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhãn hiệu có id không tồn tại");
        }
    }

    @Test
    public void testCreateProduct_SP015() throws Exception {
        Long supplierId = 999L;

        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M10");
            product.setCode("MAC-010");
            product.setSlug("macbook-m10");
            product.setShortDescription("Đây là sản phẩm Macbook M10");
            product.setDescription("Đây là sản phẩm Macbook M10");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(supplierId);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhà cung cấp có id không tồn tại");
        }
    }

    @Test
    public void testCreateProduct_SP016() throws Exception {
        Long unitId = 999L;

        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M11");
            product.setCode("MAC-011");
            product.setSlug("macbook-m11");
            product.setShortDescription("Đây là sản phẩm Macbook M11");
            product.setDescription("Đây là sản phẩm Macbook M11");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(unitId);
            product.setWeight(2.0);
            product.setGuaranteeId(1L);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Đơn vị tính có id không tồn tại");
        }
    }

    @Test
    public void testCreateProduct_SP017() throws Exception {
        Long guaranteeId = 999L;

        try {
            ProductRequest product = new ProductRequest();
            product.setName("Macbook M12");
            product.setCode("MAC-012");
            product.setSlug("macbook-m12");
            product.setShortDescription("Đây là sản phẩm Macbook M12");
            product.setDescription("Đây là sản phẩm Macbook M12");
            product.setStatus(1);
            product.setCategoryId(1L);
            product.setBrandId(1L);
            product.setSupplierId(1L);
            product.setUnitId(1L);
            product.setWeight(2.0);
            product.setGuaranteeId(guaranteeId);

            product.setImages(new ArrayList<>());
            product.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(product);

            ResponseEntity<ProductResponse> response = productController.createResource(jsonProduct);
            ProductResponse newProduct = response.getBody();
            assertThat(newProduct).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Bảo hành có id không tồn tại");
        }
    }

    @Test
    public void testUpdateProduct_SP018() throws Exception {
        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNotNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }

    }

    @Test
    public void testUpdateProduct_SP019() throws Exception {
        String name = null;

        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName(name);
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Vui lòng điền tên sản phẩm");
        }
    }

    @Test
    public void testUpdateProduct_SP020() throws Exception {
        String code = "prod-87";

        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode(code);
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã sản phẩm đã tồn tại");
        }
    }

    @Test
    public void testUpdateProduct_SP021() throws Exception {
        String code = null;

        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode(code);
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã sản phẩm không được để trống");
        }
    }

    @Test
    public void testUpdateProduct_SP022() throws Exception {
        String slug = "prod-87";

        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug(slug);
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Slug đã tồn tại");
        }
    }

    @Test
    public void testUpdateProduct_SP023() throws Exception {
        String slug = null;

        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug(slug);
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Slug không được để trống");
        }
    }

    @Test
    public void testUpdateProduct_SP024() throws Exception {
        Integer status = null;
        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(status);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Trạng thái sản phẩm không được để trống");
        }
    }

    @Test
    public void testUpdateProduct_SP025() throws Exception {
        Long categoryId = 999L;
        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(categoryId);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Danh mục sản phẩm có id không tồn tại");
        }
    }

    @Test
    public void testUpdateProduct_SP026() throws Exception {
        Long brandId = 999L;
        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(brandId);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhãn hiệu có id không tồn tại");
        }
    }

    @Test
    public void testUpdateProduct_SP027() throws Exception {
        Long supplierId = 999L;
        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(supplierId);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhà cung cấp có id không tồn tại");
        }
    }

    @Test
    public void testUpdateProduct_SP028() throws Exception {
        Long unitId = 999L;
        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(unitId);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(1L);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Đơn vị tính có id không tồn tại");
        }
    }

    @Test
    public void testUpdateProduct_SP029() throws Exception {
        Long guaranteeId = 999L;
        try {
            ProductRequest productExist = new ProductRequest();

            productExist.setName("Macbook M1 legend");
            productExist.setCode("MAC-M1-legend");
            productExist.setSlug("macbook-m1-legend");
            productExist.setShortDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setDescription("Đây là Macbook M1 legend được cập nhật");
            productExist.setStatus(1);
            productExist.setCategoryId(1L);
            productExist.setBrandId(1L);
            productExist.setSupplierId(1L);
            productExist.setUnitId(1L);
            productExist.setWeight(2.0);
            productExist.setGuaranteeId(guaranteeId);

            productExist.setImages(new ArrayList<>());
            productExist.setVariants(new ArrayList<>());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            JsonNode jsonProduct = objectMapper.valueToTree(productExist);


            ResponseEntity<ProductResponse> response = productController.updateResource(101L, jsonProduct);
            ProductResponse productUpdated = response.getBody();

            assertThat(productUpdated).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Bảo hành có id không tồn tại");
        }
    }

    @Test
    public void testDeleteProduct_SP030() throws Exception {
        Long id = 1L;
        try {
            productController.deleteResource(id);
            ResponseEntity<ProductResponse> res = productController.getResource(id);
            ProductResponse product = res.getBody();

            assertThat(product).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }
    }

    @Test
    public void testDeleteProduct_SP031() throws Exception {
        try {
            productController.deleteResource(999L);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Sản phẩm có id không tồn tại");
        }
    }

    @Test
    public void testDeleteListProduct_SP032() throws Exception {
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(1L);
            ids.add(2L);
            productController.deleteResources(ids);

            ResponseEntity<ProductResponse> res1 = productController.getResource(1L);
            ProductResponse product1 = res1.getBody();
            assertThat(product1).isNull();

            ResponseEntity<ProductResponse> res2 = productController.getResource(2L);
            ProductResponse product2 = res2.getBody();
            assertThat(product2).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }
    }

    @Test
    public void testDeleteListProduct_SP033() throws Exception {
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(999L);
            ids.add(9999L);
            productController.deleteResources(ids);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Sản phẩm có id không tồn tại");
        }
    }
}
