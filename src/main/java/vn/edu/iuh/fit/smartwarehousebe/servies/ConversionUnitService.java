package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.models.ConversionUnit;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ConversionUnitRepository;

@Service
public class ConversionUnitService extends CommonService<ConversionUnit> {

    @Autowired
    private ConversionUnitRepository conversionUnitRepository;

    public ConversionUnit create(Long toUnit, Long fromUnit,Long productId ,Double conversionRate) {
        ConversionUnit conversionUnit = ConversionUnit.builder()
                .fromUnit(Unit.builder().id(fromUnit).build())
                .toUnit(Unit.builder().id(toUnit).build())
                .product(Product.builder().id(productId).build())
                .conversionRate(conversionRate)
                .build();
        return conversionUnitRepository.save(conversionUnit);
    }

    public boolean delete(Long id) {
        try {
            conversionUnitRepository.deleteById(id);
            return true;
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return false;
    }
}
