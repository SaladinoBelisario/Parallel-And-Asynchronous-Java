package com.example.competablefuture;

import com.example.domain.Product;
import com.example.domain.ProductOption;
import com.example.service.InventoryService;
import com.example.service.ProductInfoService;
import com.example.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUsingCompletableFutureExceptionTest {


    @Mock
    ProductInfoService pisMock;
    @Mock
    ReviewService rssMock;
    @Mock
    InventoryService isMock;

    @InjectMocks
    ProductServiceUsingCompletableFuture pscf;

    @Test
    void retrieveProductDetails_reviewServiceError() {

        //given
        String productId = "ABC123";
        when(pisMock.retrieveProductInfo(any())).thenCallRealMethod();
        when(rssMock.retrieveReviews(any())).thenThrow(new RuntimeException("Exception Occurred"));
        when(isMock.retrieveInventory(any(ProductOption.class))).thenCallRealMethod();


        //when
        Product product = pscf.retrieveProductDetailsWithInventory_approach2(productId);

        //then
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
        });
        assertNotNull(product.getReview());
        assertEquals(0, product.getReview().getNoOfReviews());

        long count = product.getProductInfo().getProductOptions().size();
        System.out.println("count : "+count);

    }

    @Test
    void retrieveProductDetails_productInfoServiceError() {

        //given
        String productId = "ABC123";
        when(pisMock.retrieveProductInfo(any())).thenThrow(new RuntimeException("Exception Occurred"));
        when(rssMock.retrieveReviews(any())).thenCallRealMethod();

        //when
        Assertions.assertThrows(RuntimeException.class, ()->pscf.retrieveProductDetailsWithInventory_approach2(productId));

    }

    @Test
    void retrieveProductDetails_inteventoryServiceError() {

        //given
        String productId = "ABC123";
        when(pisMock.retrieveProductInfo(any())).thenCallRealMethod();
        when(rssMock.retrieveReviews(any())).thenCallRealMethod();
        when(isMock.retrieveInventory(any(ProductOption.class))).thenThrow(new RuntimeException("Exception Occurred"));


        //when
        Product product = pscf.retrieveProductDetailsWithInventory_approach2(productId);

        //then
        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(productOption -> {
            assertNotNull(productOption.getInventory());
            assertEquals(1, productOption.getInventory().getCount());

        });
        assertNotNull(product.getReview());

    }
}