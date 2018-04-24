package practice2;

import java.math.BigDecimal;
import java.util.List;

public class Receipt {

    public Receipt() {
        tax = new BigDecimal(0.1);
        tax = tax.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal tax;

    public double CalculateGrandTotal(List<Product> products, List<OrderItem> items) {
        BigDecimal subTotal = calculateSubtotal(products, items);

        BigDecimal grandTotal = getTaxTotal(products, items, subTotal);

        return grandTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private BigDecimal getTaxTotal(List<Product> products, List<OrderItem> items, BigDecimal subTotal) {
        for (Product product : products) {
            subTotal = getReducePrice(items, subTotal, product);
        }
        BigDecimal taxTotal = subTotal.multiply(tax);
        return subTotal.add(taxTotal);
    }

    private BigDecimal getReducePrice(List<OrderItem> items, BigDecimal subTotal, Product product) {
        OrderItem curItem = findOrderItemByProduct(items, product);

        BigDecimal reducedPrice = product.getPrice()
                .multiply(product.getDiscountRate())
                .multiply(new BigDecimal(curItem.getCount()));

        subTotal = subTotal.subtract(reducedPrice);
        return subTotal;
    }


    private OrderItem findOrderItemByProduct(List<OrderItem> items, Product product) {
        OrderItem curItem = null;
        for (OrderItem item : items) {
            if (item.getCode() == product.getCode()) {
                curItem = item;
                break;
            }
        }
        return curItem;
    }

    private BigDecimal calculateSubtotal(List<Product> products, List<OrderItem> items) {
        BigDecimal subTotal = new BigDecimal(0);
        for (Product product : products) {
            subTotal = getItemTotal(items, subTotal, product);
        }
        return subTotal;
    }

    private BigDecimal getItemTotal(List<OrderItem> items, BigDecimal subTotal, Product product) {
        OrderItem item = findOrderItemByProduct(items, product);
        BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(item.getCount()));
        subTotal = subTotal.add(itemTotal);
        return subTotal;
    }
}
