package top.iotequ.ewallet.utility;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import top.iotequ.ewallet.dao.EwBillDao;
import top.iotequ.ewallet.pojo.EwBill;

/**
 * Generate auto-increment bill number.
 * The format of the bill number is a 16-digit-number:
 * yyyyMMddxxxxxxxx, where 'x' is the auto-increment number
 * from 1 to 99999999 for a day (24 hour) cycle.
 * Created by ao on 2019-07-30
 */
@Component
public class BillNumberGenerator implements ApplicationContextAware {

    @Autowired
    private EwBillDao ewBillDao;

    private int incrementNumber;

    private LocalDate currentDate;

    String generateNextBillNumber() {

        if (!LocalDate.now().equals(this.currentDate)) {
            this.currentDate = LocalDate.now();
            this.incrementNumber = 1;
        }

        String billNumber = this.currentDate.format(BASIC_ISO_DATE)
                .concat(String.format("%08d", this.incrementNumber));

        this.incrementNumber++;

        return billNumber;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        try {
            List<EwBill> list = ewBillDao.query("select max(no) as no from ew_bill");

            if (Objects.isNull(list) || list.isEmpty() || Objects.isNull(list.get(0))) {
                this.currentDate = LocalDate.now();
                this.incrementNumber = 1;
                return;
            }

            String maxNo = list.get(0).getNo();
            int year = Integer.parseInt(maxNo.substring(0, 4));
            int month = Integer.parseInt(maxNo.substring(4, 6));
            int day = Integer.parseInt(maxNo.substring(6, 8));

            LocalDate localDate = LocalDate.of(year, month, day);

            if (LocalDate.now().equals(localDate)) {
                this.currentDate = localDate;
                this.incrementNumber = Integer.valueOf(maxNo.substring(8)) + 1;
            } else {
                this.currentDate = LocalDate.now();
                this.incrementNumber = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
