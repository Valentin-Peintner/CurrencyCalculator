import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class calculator {
    private static JToggleButton selectedButton = null;
    static String selectedCurrency = "";
    static JLabel text = new JLabel("Bitte gebe einen Betrag an in Euro an:");
    static JLabel logo = new JLabel("€");
    static JTextField textField = new JTextField();
    static JLabel textResult = new JLabel();
    static JButton convert = new JButton("Rechnen");

    public static void main(String[] args) {
        openUI();
    }

    public static void openUI() {
        JFrame frame = new JFrame("Wechselkursrechner");
        frame.setSize(500, 600);
        frame.setLocation(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // UI-config
        text.setFont(new Font("Arial", Font.PLAIN, 18));
        text.setBounds(100, 60, 400, 30);
        logo.setBounds(180, 140, 120, 40);
        logo.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setBounds(200, 140, 100, 40);
        textResult.setBounds(180, 300, 220, 40);
        convert.setBounds(200, 180, 100, 40);
        frame.getRootPane().setDefaultButton(convert);

        convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedCurrency == null || selectedCurrency.isEmpty()) {
                    text.setText("Bitte wähle eine Währung aus!");
                    return;
                }
                try {
                    String enteredText = textField.getText();
                    Double number = Double.parseDouble(enteredText);
                    currencyExchange(number);
                } catch (NumberFormatException error) {
                    text.setText("Bitte gebe eine gültige Zahl ein!");
                }
            }
        });

        // Buttons for Currency
        // Japan Yen
        JToggleButton jypButton = new JToggleButton("YEN");
        JToggleButton dollarButton = new JToggleButton("DOLLAR");
        JToggleButton chfButton = new JToggleButton("CHF");

        configureButton(jypButton, "YEN");
        configureButton(dollarButton, "DOLLAR");
        configureButton(chfButton, "CHF");

        frame.add(text);
        frame.add(textField);
        frame.add(textResult);
        frame.add(logo);
        frame.add(convert);
        frame.add(jypButton);
        frame.add(dollarButton);
        frame.add(chfButton);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static void configureButton(JToggleButton button, String currency) {
        button.setFont(new Font("Arial", Font.PLAIN, 15));
        int xOffset = 50;
        int buttonWidth = 120;
        int buttonHeight = 30;
        int spacing = 20;
        switch (currency) {
            case "DOLLAR":
                xOffset += buttonWidth + spacing;
                break;
            case "CHF":
                xOffset += (buttonWidth + spacing) * 2; // spacing
                break;
            default:
                break;
        }

        button.setBounds(xOffset, 90, buttonWidth, buttonHeight);
        button.setBackground(Color.LIGHT_GRAY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleButtonSelection(button, currency);
            }
        });
    }


    private static void handleButtonSelection(JToggleButton button, String currency) {
        if (selectedButton != null && selectedButton != button) {
            selectedButton.setSelected(false);
            selectedButton.setBorderPainted(false);
            selectedButton.setBackground(Color.LIGHT_GRAY);
        }
        selectedButton = button;
        selectedCurrency = currency;

        if (button.isSelected()) {
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            button.setBackground(Color.GREEN);
        } else {
            button.setBorderPainted(false);
            button.setBackground(Color.LIGHT_GRAY);
        }
    }

    public static void currencyExchange(Double eur) {
        try {
            Double result = 0.00;
            NumberFormat formatter;
            String currencySymbol = "";

            // Mapping
            String apiCurrencyCode = "";
            switch (selectedCurrency) {
                case "YEN":
                    apiCurrencyCode = "JPY";
                    currencySymbol = "¥";
                    break;
                case "DOLLAR":
                    apiCurrencyCode = "USD";
                    break;
                case "CHF":
                    apiCurrencyCode = "CHF";
                    currencySymbol = "CHF";
                    break;
                default:
                    textResult.setText("Unbekannte Währung");
                    return;
            }

            // Fetch exchange rate
            double exchangeRate = exchangeRateFetcher.getExchangeRate(apiCurrencyCode);

            result = eur * exchangeRate;

            // Format
            if (apiCurrencyCode.equals("JPY")) {
                formatter = NumberFormat.getInstance();
                formatter.setMaximumFractionDigits(2);
            } else if (apiCurrencyCode.equals("USD")) {
                formatter = NumberFormat.getCurrencyInstance();
                formatter.setCurrency(java.util.Currency.getInstance("USD"));
            } else {
                formatter = NumberFormat.getInstance();
                formatter.setMaximumFractionDigits(2);
            }

            String formattedResult = currencySymbol + " " + formatter.format(result);
            textResult.setText("Der Betrag ist: " + formattedResult);

        } catch (Exception e) {
            textResult.setText("Fehler bei der Währungsumrechnung.");
            e.printStackTrace();
        }
    }

}
