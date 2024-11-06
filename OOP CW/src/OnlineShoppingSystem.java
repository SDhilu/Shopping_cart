import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class Product {
    private String productId;
    private String productName;
    private int availableItems;
    private double price;

    public Product(String productId, String productName, int availableItems, double price) {
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getProductType();
}

class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;

    public Electronics(String productId, String productName, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productId, productName, availableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    @Override
    public String getProductType() {
        return "Electronics";
    }
}

class Clothing extends Product {
    private String size;
    private String color;

    public Clothing(String productId, String productName, int availableItems, double price, String size, String color) {
        super(productId, productName, availableItems, price);
        this.size = size;
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String getProductType() {
        return "Clothing";
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    // Password-related methods
}

class ShoppingCart {
    private List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public double calculateTotalCost() {
        double totalCost = 0.0;
        for (Product product : products) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }

    public List<Product> getProducts() {
        return products;
    }
}

interface ShoppingManager {
    void addProduct(Product product);

    void deleteProduct(String productId);

    void printProducts();

    void saveProductsToFile();
}

class WestminsterShoppingManager implements ShoppingManager {
    private List<Product> productList;

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return productList;
    }

    @Override
    public void addProduct(Product product) {
        productList.add(product);
    }

    @Override
    public void deleteProduct(String productId) {
        Product productToRemove = null;
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                productToRemove = product;
                break;
            }
        }

        if (productToRemove != null) {
            productList.remove(productToRemove);
            System.out.println("Product deleted: " + productToRemove.getProductName());
            System.out.println("Total products remaining: " + productList.size());
        } else {
            System.out.println("Product not found with ID: " + productId);
        }
    }

    @Override
    public void printProducts() {
        Collections.sort(productList, (p1, p2) -> p1.getProductId().compareTo(p2.getProductId()));

        for (Product product : productList) {
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Available Items: " + product.getAvailableItems());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Type: " + product.getProductType());
            System.out.println("--------------------");
        }
    }

    @Override
    public void saveProductsToFile() {
        // Implement saving products to a file
    }
}

class ShoppingCartGUI extends JFrame {
    private DefaultListModel<Product> productListModel;
    private JList<Product> productList;
    private JTextArea productDetailsArea;
    private JButton addToCartButton;
    private JButton viewShoppingCartButton;
    private ShoppingCart shoppingCart;
    private JTextArea shoppingCartTextArea;

    public ShoppingCartGUI() {
        super("Online Shopping System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        JScrollPane productScrollPane = new JScrollPane(productList);
        productScrollPane.setPreferredSize(new Dimension(300, 300));

        productDetailsArea = new JTextArea();
        productDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(productDetailsArea);
        detailsScrollPane.setPreferredSize(new Dimension(300, 80));

        addToCartButton = new JButton("Add to Cart");
        viewShoppingCartButton = new JButton("View Shopping Cart");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(productScrollPane, BorderLayout.CENTER);
        panel.add(detailsScrollPane, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewShoppingCartButton);

        add(panel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);

        shoppingCart = new ShoppingCart();


        shoppingCartTextArea = new JTextArea();
        shoppingCartTextArea.setEditable(false);
        JScrollPane cartScrollPane = new JScrollPane(shoppingCartTextArea);
        cartScrollPane.setPreferredSize(new Dimension(300, 120));

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        add(cartPanel, BorderLayout.EAST);


        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = productList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Product selectedProduct = productList.getSelectedValue();
                    shoppingCart.addProduct(selectedProduct);
                    updateShoppingCartButton();
                }
            }
        });

        viewShoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showShoppingCartDialog();
            }
        });
    }

    public void updateProductList(List<Product> products) {
        productListModel.clear();
        for (Product product : products) {
            productListModel.addElement(product);
        }
    }

    private void updateShoppingCartButton() {
        viewShoppingCartButton.setText("View Shopping Cart (" + shoppingCart.getProducts().size() + ")");
    }

    private void showShoppingCartDialog() {
        StringBuilder cartContent = new StringBuilder("Shopping Cart:\n");
        for (Product product : shoppingCart.getProducts()) {
            cartContent.append(product.getProductName()).append(" - $").append(product.getPrice()).append("\n");
        }
        cartContent.append("\nTotal Cost: $").append(shoppingCart.calculateTotalCost());

        shoppingCartTextArea.setText(cartContent.toString());

        JOptionPane.showMessageDialog(this, cartContent.toString(), "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
    }
}

public class OnlineShoppingSystem {
    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        // Add sample products to the manager
        shoppingManager.addProduct(new Electronics("E001", "Laptop", 10, 999.99, "Dell", 1));
        shoppingManager.addProduct(new Clothing("C001", "T-Shirt", 20, 19.99, "M", "Red"));

        // Create GUI
        ShoppingCartGUI shoppingCartGUI = new ShoppingCartGUI();

        // Update GUI product list based on the manager's product list
        shoppingCartGUI.updateProductList(shoppingManager.getProducts());

        // Display GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                shoppingCartGUI.setVisible(true);
            }
        });
    }
}
