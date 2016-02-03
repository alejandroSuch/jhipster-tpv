package such.alex.tpv.service.reactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.fn.tuple.Tuple2;
import reactor.spring.context.annotation.Consumer;
import reactor.spring.context.annotation.ReplyTo;
import reactor.spring.context.annotation.Selector;
import such.alex.tpv.domain.Category;
import such.alex.tpv.domain.Vat;
import such.alex.tpv.service.CategoryService;
import such.alex.tpv.service.util.Constants;

import java.util.List;

/**
 * Created by alejandro on 2/2/16.
 */
@Consumer
@Transactional
public class CategoryHandler {
    @Autowired
    CategoryService categoryService;

    @Selector(eventBus = "@eventBus", value = Constants.VAT_UPDATED)
    @ReplyTo(Constants.CATEGORIES_UPDATED)
    public List<Category> handleVatUpdated(Tuple2<Vat, Vat> data) {
        Vat oldVat = data.getT1();
        Vat newVat = data.getT2();

        List<Category> categories = categoryService.findAllByVat(oldVat);
        for (Category category : categories) {
            category.setVat(newVat);
            categoryService.save(category);
        }

        return categories;
    }
}
