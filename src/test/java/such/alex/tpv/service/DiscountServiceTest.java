package such.alex.tpv.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import such.alex.tpv.Application;
import such.alex.tpv.domain.Discount;
import such.alex.tpv.domain.Product;
import such.alex.tpv.repository.ProductRepository;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by alejandro on 12/2/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class DiscountServiceTest {
    @Inject
    ProductService productService;

    @Inject
    ProductRepository productRepository;

    @Inject
    DiscountService discountService;

    @Test
    public void getsADiscountForAGivenProduct(){
        final Product product = setupProductWithTwoDiscounts();
        final Discount activeDiscountForProduct = discountService.getActiveDiscountForProduct(product);

        assertThat(activeDiscountForProduct.getValue()).isEqualTo(5f);
    }

    @Test
    public void getsNoDiscountForAGivenProduct(){
        final Product product = setupProductWithTwoFutureDiscounts();
        final Discount activeDiscountForProduct = discountService.getActiveDiscountForProduct(product);

        assertThat(activeDiscountForProduct.getValue()).isNull();
    }

    private Product setupProductWithTwoDiscounts() {
        final LocalDate now = LocalDate.now();

        Product product = new Product()
            .setName("name")
            .setCode("1111111111111")
            .setDescription("product description");


        Discount firstDiscount = new Discount()
            .setActiveFrom(now.minusDays(3))
            .setActiveTo(now.plusDays(3))
            .setValue(5f)
            .setDescription("second discount")
            .setUnits(3)
            .setCode("secondDiscount");

        Discount secondDiscount = new Discount()
            .setActiveFrom(now.plusDays(4))
            .setActiveTo(now.plusDays(7))
            .setValue(1f)
            .setDescription("second discount")
            .setUnits(3)
            .setCode("secondDiscount");

        final Set<Discount> discounts = product.getDiscounts();

        discounts.add(firstDiscount);
        discounts.add(secondDiscount);

        return productService.save(product);
    }

    private Product setupProductWithTwoFutureDiscounts() {
        final LocalDate now = LocalDate.now();

        Product product = new Product()
            .setName("name")
            .setCode("1111111111111")
            .setDescription("product description");


        Discount firstDiscount = new Discount()
            .setActiveFrom(now.plusDays(4))
            .setActiveTo(now.plusDays(7))
            .setValue(5f)
            .setDescription("second discount")
            .setUnits(3)
            .setCode("secondDiscount");

        Discount secondDiscount = new Discount()
            .setActiveFrom(now.plusDays(10))
            .setActiveTo(now.plusDays(20))
            .setValue(1f)
            .setDescription("second discount")
            .setUnits(3)
            .setCode("secondDiscount");

        final Set<Discount> discounts = product.getDiscounts();

        discounts.add(firstDiscount);
        discounts.add(secondDiscount);

        return productService.save(product);
    }
}
