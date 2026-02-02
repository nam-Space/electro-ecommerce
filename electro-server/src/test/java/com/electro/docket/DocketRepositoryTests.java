package com.electro.docket;

import com.electro.constant.ResourceName;
import com.electro.constant.SearchFields;
import com.electro.controller.GenericController;
import com.electro.dto.ListResponse;
import com.electro.dto.inventory.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.electro.dto.product.ProductRequest;
import com.electro.dto.product.ProductResponse;
import com.electro.entity.inventory.Docket;
import com.electro.entity.inventory.DocketReason;
import com.electro.entity.inventory.PurchaseOrder;
import com.electro.entity.inventory.Warehouse;
import com.electro.entity.product.Product;
import com.electro.mapper.inventory.DocketMapper;
import com.electro.mapper.product.ProductMapper;
import com.electro.repository.inventory.DocketRepository;
import com.electro.repository.product.ProductRepository;
import com.electro.service.GenericService;
import com.electro.service.inventory.DocketService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.electro.service.inventory.DocketServiceImpl;

import javax.transaction.Transactional;
import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DocketRepositoryTests {
    @Autowired
    private DocketServiceImpl docketRepositoryImpl;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private GenericService<Docket, DocketRequest, DocketResponse> docketService;

    @Autowired
    private GenericController<DocketRequest, DocketResponse> docketController;

    @BeforeEach
    void initData() {
        docketController.setCrudService(docketService.init(
                context.getBean(DocketRepository.class),
                context.getBean(DocketMapper.class),
                SearchFields.DOCKET,
                ResourceName.DOCKET));
        docketController.setRequestType(DocketRequest.class);
    }

    @Test
    @Transactional
    public void testGetAllDocket_NX001() throws Exception {
        int size = 5;
        ListResponse<DocketResponse> dockets = docketRepositoryImpl.findAll(1, size, null, null, null, false);
        assertThat(dockets.getSize()).isEqualTo(size);
        System.out.println("Expected size: " + size);
        System.out.println("Actual size: " + dockets.getSize());
    }

    @Test
    @Transactional
    public void testGetAllDocket_NX002() throws Exception {
        int size = 0;
        try {
            ListResponse<DocketResponse> dockets = docketRepositoryImpl.findAll(1, size, null, null, null, false);
            assertThat(dockets.getSize()).isEqualTo(size);
            System.out.println("Expected size: " + size);
            System.out.println("Actual size: " + dockets.getSize());
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Danh sách phiếu nhập xuất kho rỗng");
        }
    }

    @Test
    @Transactional
    public void testGetAllDocket_NX003() throws Exception {
        int size = -1;
        try {
            ListResponse<DocketResponse> dockets = docketRepositoryImpl.findAll(1, size, null, null, null, false);
            assertThat(dockets.getSize()).isEqualTo(size);
            System.out.println("Expected size: " + size);
            System.out.println("Actual size: " + dockets.getSize());
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Danh sách phiếu nhập xuất kho rỗng");
        }
    }

    @Test
    @Transactional
    public void testGetDocketById_NX004() throws Exception {
        DocketResponse docket = docketRepositoryImpl.findById(1L);
        assertThat(docket).isNotNull();
        System.out.println("Docket " + docket);
    }

    @Test
    @Transactional
    public void testGetDocketById_NX005() throws Exception {
        Long id = 99L;
        try {
            DocketResponse docket = docketRepositoryImpl.findById(id);
            assertThat(docket).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Phiếu nhập xuất kho có id không tồn tại");
        }
    }

    @Test
    public void testCreateDocket_NX006() throws Exception {
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            Set<DocketVariantRequest> docketVariants = new HashSet<>();
            DocketVariantRequest docketVariant1 = new DocketVariantRequest();
            docketVariant1.setVariantId(102L);
            docketVariant1.setQuantity(1);
            DocketVariantRequest docketVariant2 = new DocketVariantRequest();
            docketVariant2.setVariantId(103L);
            docketVariant2.setQuantity(1);

            docketVariants.add(docketVariant1);
            docketVariants.add(docketVariant2);
            request.setDocketVariants(docketVariants);

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNotNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }
    }

    @Test
    public void testCreateDocket_NX007() throws Exception {
        Integer type = null;

        try {
            DocketRequest request = new DocketRequest();
            request.setType(type);
            request.setCode("SKS26-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            Set<DocketVariantRequest> docketVariants = new HashSet<>();
            DocketVariantRequest docketVariant1 = new DocketVariantRequest();
            docketVariant1.setVariantId(102L);
            docketVariant1.setQuantity(1);
            DocketVariantRequest docketVariant2 = new DocketVariantRequest();
            docketVariant2.setVariantId(103L);
            docketVariant2.setQuantity(1);

            docketVariants.add(docketVariant1);
            docketVariants.add(docketVariant2);
            request.setDocketVariants(docketVariants);

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Loại phiếu nhập xuất kho không được để trống");
        }
    }

    // Test code bị trùng
    @Test
    public void testCreateDocket_NX008() throws Exception {
        String code = "68016-008";

        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode(code);
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            Set<DocketVariantRequest> docketVariants = new HashSet<>();
            DocketVariantRequest docketVariant1 = new DocketVariantRequest();
            docketVariant1.setVariantId(102L);
            docketVariant1.setQuantity(1);
            DocketVariantRequest docketVariant2 = new DocketVariantRequest();
            docketVariant2.setVariantId(103L);
            docketVariant2.setQuantity(1);

            docketVariants.add(docketVariant1);
            docketVariants.add(docketVariant2);
            request.setDocketVariants(docketVariants);

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã phiếu nhập xuất kho đã tồn tại");
        }
    }

    @Test
    public void testCreateDocket_NX009() throws Exception {
        String code = null;

        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode(code);
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            Set<DocketVariantRequest> docketVariants = new HashSet<>();
            DocketVariantRequest docketVariant1 = new DocketVariantRequest();
            docketVariant1.setVariantId(102L);
            docketVariant1.setQuantity(1);
            DocketVariantRequest docketVariant2 = new DocketVariantRequest();
            docketVariant2.setVariantId(103L);
            docketVariant2.setQuantity(1);

            docketVariants.add(docketVariant1);
            docketVariants.add(docketVariant2);
            request.setDocketVariants(docketVariants);

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã phiếu nhập xuất kho không được để trống");
        }
    }

    @Test
    public void testCreateDocket_NX010() throws Exception {
        Long reasonId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS27-TTUIP");
            request.setReasonId(reasonId);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lý do phiếu nhập xuất kho có id không tồn tại");
        }
    }

    @Test
    public void testCreateDocket_NX011() throws Exception {
        Long reasonId = null;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS28-TTUIP");
            request.setReasonId(reasonId);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lý do phiếu nhập xuất kho không được để trống");
        }
    }

    @Test
    public void testCreateDocket_NX012() throws Exception {
        Long warehouseId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS29-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(warehouseId);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhà kho có id không tồn tại");
        }
    }

    @Test
    public void testCreateDocket_NX013() throws Exception {
        Long warehouseId = null;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS30-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(warehouseId);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhà kho không được để trống");
        }
    }

    @Test
    public void testCreateDocket_NX014() throws Exception {
        Long purchaseOrderId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS31-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(purchaseOrderId);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Đơn mua hàng có id không tồn tại");
        }
    }

    @Test
    public void testCreateDocket_NX015() throws Exception {
        Long orderId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS32-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(orderId);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Đơn hàng có id không tồn tại");
        }
    }

    @Test
    public void testCreateDocket_NX016() throws Exception {
        Integer status = null;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS33-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(status);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Trạng thái đơn hàng không được để trống");
        }
    }

    @Test
    public void testCreateDocket_NX017() throws Exception {
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS34-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm chính hãng");
            request.setStatus(3);
            request.setDocketVariants(new HashSet<>());

            DocketResponse docketResponse = docketRepositoryImpl.save(request);
            assertThat(docketResponse).isNull();
            System.out.println("Vui lòng thêm sản phẩm");
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Vui lòng thêm sản phẩm");
        }
    }

    @Test
    public void testUpdateDocket_NX018() throws Exception {
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            Set<DocketVariantRequest> docketVariants = new HashSet<>();
            DocketVariantRequest docketVariant1 = new DocketVariantRequest();
            docketVariant1.setVariantId(102L);
            docketVariant1.setQuantity(1);
            DocketVariantRequest docketVariant2 = new DocketVariantRequest();
            docketVariant2.setVariantId(103L);
            docketVariant2.setQuantity(1);

            docketVariants.add(docketVariant1);
            docketVariants.add(docketVariant2);
            request.setDocketVariants(docketVariants);

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            assertThat(response).isNotNull();
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }
    }

    @Test
    public void testUpdateDocket_NX019() throws Exception {
        Integer type = null;

        try {
            DocketRequest request = new DocketRequest();
            request.setType(type);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            Set<DocketVariantRequest> docketVariants = new HashSet<>();
            DocketVariantRequest docketVariant1 = new DocketVariantRequest();
            docketVariant1.setVariantId(102L);
            docketVariant1.setQuantity(1);
            DocketVariantRequest docketVariant2 = new DocketVariantRequest();
            docketVariant2.setVariantId(103L);
            docketVariant2.setQuantity(1);

            docketVariants.add(docketVariant1);
            docketVariants.add(docketVariant2);
            request.setDocketVariants(docketVariants);

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            assertThat(response).isNull();
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Loại phiếu nhập xuất kho không được để trống");
        }
    }

    // Test code bị trùng
    @Test
    public void testUpdateDocket_NX020() throws Exception {
        String code = "68016-008";

        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode(code);
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            assertThat(response).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã phiếu nhập xuất kho đã tồn tại");
        }
    }

    @Test
    public void testUpdateDocket_NX021() throws Exception {
        String code = null;

        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode(code);
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            assertThat(response).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Mã phiếu nhập xuất kho không được để trống");
        }
    }

    @Test
    public void testUpdateDocket_NX022() throws Exception {
        Long reasonId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(reasonId);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lý do phiếu nhập xuất kho có id không tồn tại");
        }
    }

    @Test
    public void testUpdateDocket_NX023() throws Exception {
        Long reasonId = null;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(reasonId);
            request.setWarehouseId(1L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lý do phiếu nhập xuất kho không được để trống");
        }
    }

    @Test
    public void testUpdateDocket_NX024() throws Exception {
        Long warehouseId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(warehouseId);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhà kho có id không tồn tại");
        }
    }

    @Test
    public void testUpdateDocket_NX025() throws Exception {
        Long warehouseId = null;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(warehouseId);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Nhà kho không được để trống");
        }
    }

    @Test
    public void testUpdateDocket_NX026() throws Exception {
        Long purchaseOrderId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(purchaseOrderId);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Đơn mua hàng có id không tồn tại");
        }
    }

    @Test
    public void testUpdateDocket_NX027() throws Exception {
        Long orderId = 99L;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(orderId);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Đơn hàng có id không tồn tại");
        }
    }

    @Test
    public void testUpdateDocket_NX028() throws Exception {
        Integer status = null;
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(status);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            System.out.println("docketResponse: " + response);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Trạng thái phiếu nhập xuất kho không được để trống");
        }
    }

    @Test
    public void testUpdateDocket_NX029() throws Exception {
        try {
            DocketRequest request = new DocketRequest();
            request.setType(1);
            request.setCode("SKS25-TTUIP");
            request.setReasonId(1L);
            request.setWarehouseId(2L);
            request.setPurchaseOrderId(1L);
            request.setOrderId(1L);
            request.setNote("Đây là sản phẩm mới đã sửa");
            request.setStatus(1);

            request.setDocketVariants(new HashSet<>());

            DocketResponse response = docketRepositoryImpl.save(15L, request);
            assertThat(response).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Vui lòng thêm sản phẩm");
        }
    }


    @Test
    public void testDeleteDocket_NX030() throws Exception {
        Long id = 1L;
        try {
            docketRepositoryImpl.delete(id);
            DocketResponse docket = docketRepositoryImpl.findById(id);
            assertThat(docket).isNull();
            System.out.println("Docket " + docket);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }
    }

    @Test
    public void testDeleteDocket_NX031() throws Exception {
        try {
            docketRepositoryImpl.delete(99L);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Phiếu nhập xuất kho có id không tồn tại");
        }
    }

    @Test
    public void testDeleteListDocket_NX032() throws Exception {
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(1L);
            ids.add(2L);
            docketRepositoryImpl.delete(ids);

            DocketResponse docket1 = docketRepositoryImpl.findById(1L);
            assertThat(docket1).isNull();
            DocketResponse docket2 = docketRepositoryImpl.findById(2L);
            assertThat(docket2).isNull();
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Lỗi kết nối, xin vui lòng thử lại");
        }
    }

    @Test
    public void testDeleteListDocket_NX033() throws Exception {
        try {
            List<Long> ids = new ArrayList<>();
            ids.add(99L);
            ids.add(999L);
            docketRepositoryImpl.delete(ids);
        }
        catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Phiếu nhập xuất kho có id không tồn tại");
        }
    }
}