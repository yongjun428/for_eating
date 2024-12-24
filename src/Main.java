import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

/**
 * @author yongjun428 (김용준, 2021012011)
 *
 *
 * @created 2024-12-22
 *  @see <a href="https://chatgpt.com/">ChatGPT</a>
 *
 * @changelog
 * <ul>
 *     <li> 2024-12-22 : 기본틀 을 생성  csv를 사용하여 한식,중식,일식,양식으로 음식종류를 구별하고
 *          카테고리를 사용하여 면,밥,빵 으로 다양하게 고를 수 있게 만듬
 *     </li>
 *     <li> 2024-12-23 : 음식점 데이터를 food/Restaurant.csv 파일에 저장하고, 로드하는 기능 추가.  </li>
 *     <li>
 *         2024-12-23 : .카테고리를 없애고"한식,양식,중식,일식, 패스트푸드, 카페, 분식, 야식, 고기" 를 추가
 *     </li>
 *     <li>
 *         2024-12-23 : 음식점 추가 창: JFrame을 사용해 음식점 종류, 이름을 입력받아 저장,
 *                      음식점 조회 창: JFrame에서 음식 종류를 선택하여 관련 음식점 목록 조회.
 *     </li>
 *     <li>
 *         2024-12-24 : 음식점 데이터를 관리하기 위해 restaurantMap 데이터를 HashMap으로 저장.
 *                      HashMap<String, ArrayList<String>> 사용
 *                      키: 음식 종류
 *                      값: 해당 음식 종류의 음식점 이름 목록.
 *     </li>
 *     <li>
 *         2024-12-25: 조회 창에서 음식점 이름과 링크를 함께 표시하고 링크는 클릭 가능한 형태로 HTML로 바꿈.
 *                      JTextArea를 JTextPane으로 변경하여 HTML 지원 및 링크 클릭 처리.
 *     </li>
 * </ul>
 */

/**
 * 음식점 관리 프로그램.
 * <p>
 * 음식점을 추가하거나 조회할 수 있으며, 데이터를 CSV 파일로 저장하고 로드합니다.
 * GUI를 통해 사용자에게 직관적인 환경을 제공합니다.
 */
public class Main {
    /**
     * 음식점 데이터를 저장하는 CSV 파일의 경로.
     */
    private static final String FILE_NAME = "food/Restaurant.csv";

    /**
     * 음식점 데이터를 관리하는 컬렉션.
     * <p>
     * 키는 음식 종류(String), 값은 음식점 이름과 링크(String[]) 목록(ArrayList).
     */
    private static final HashMap<String, ArrayList<String[]>> restaurantMap = new HashMap<>();

    /**
     * 프로그램의 진입점.
     * <p>
     * CSV 파일에서 데이터를 로드하고 GUI를 생성합니다.
     *
     * @param args 명령줄 인수 (사용되지 않음).
     */
    public static void main(String[] args) {
        loadRestaurantsFromCSV(FILE_NAME); // CSV 파일에서 데이터 로드
        SwingUtilities.invokeLater(Main::createAndShowGUI); // GUI 시작
    }

    /**
     * 메인 GUI 창을 생성하고 표시합니다.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("음식점 관리 프로그램");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton viewButton = new JButton("음식점 조회");
        JButton addButton = new JButton("음식점 추가");
        JButton exitButton = new JButton("종료");

        // 버튼 이벤트 연결
        viewButton.addActionListener(e -> openViewRestaurantsWindow());
        addButton.addActionListener(e -> openAddRestaurantWindow());
        exitButton.addActionListener(e -> System.exit(0));

        // 패널에 버튼 추가
        mainPanel.add(viewButton);
        mainPanel.add(addButton);
        mainPanel.add(exitButton);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * 음식점 조회 창을 생성하고 표시합니다.
     * <p>
     * 사용자가 선택한 음식 종류에 따라 관련 음식점과 링크를 표시합니다.
     */
    private static void openViewRestaurantsWindow() {
        JFrame viewFrame = new JFrame("음식점 조회");
        viewFrame.setSize(600, 600);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JLabel comboLabel = new JLabel("조회할 음식 종류를 선택하세요:");
        JComboBox<String> comboBox = new JComboBox<>(getFoodTypes());
        comboBox.setPreferredSize(new Dimension(200, 25));

        JTextPane resultPane = new JTextPane();
        resultPane.setContentType("text/html");
        resultPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setPreferredSize(new Dimension(550, 400));

        JButton searchButton = new JButton("조회");
        searchButton.addActionListener(e -> {
            String selectedType = (String) comboBox.getSelectedItem();
            if (selectedType != null && restaurantMap.containsKey(selectedType)) {
                ArrayList<String[]> restaurants = restaurantMap.get(selectedType);
                if (!restaurants.isEmpty()) {
                    StringBuilder resultHtml = new StringBuilder("<html><body style='font-family: Arial;'>");
                    for (String[] restaurant : restaurants) {
                        resultHtml.append("<p>")
                                .append(restaurant[0]) // 음식점 이름
                                .append(" (<a href='")
                                .append(restaurant[1]) // 링크
                                .append("'>")
                                .append(restaurant[1]) // 링크 텍스트
                                .append("</a>)</p>");
                    }
                    resultHtml.append("</body></html>");
                    resultPane.setText(resultHtml.toString());
                    resultPane.setCaretPosition(0);
                } else {
                    resultPane.setText("<html><body>해당 음식 종류에 맞는 음식점이 없습니다.</body></html>");
                }
            } else {
                resultPane.setText("<html><body>음식 종류를 선택해주세요.</body></html>");
            }
        });

        resultPane.addHyperlinkListener(event -> {
            if (event.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new java.net.URI(event.getURL().toString()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(viewFrame, "링크를 열 수 없습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(comboLabel);
        topPanel.add(comboBox);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(searchButton);

        viewFrame.add(panel, BorderLayout.CENTER);
        viewFrame.add(bottomPanel, BorderLayout.SOUTH);

        viewFrame.setVisible(true);
    }

    /**
     * 음식점 추가 창을 생성하고 표시합니다.
     * <p>
     * 사용자가 입력한 음식 종류, 이름, 링크를 기반으로 데이터를 추가합니다.
     */
    private static void openAddRestaurantWindow() {
        JFrame addFrame = new JFrame("음식점 추가");
        addFrame.setSize(400, 400);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel foodTypeLabel = new JLabel("음식 종류:");
        JTextField foodTypeField = new JTextField();
        JLabel nameLabel = new JLabel("음식점 이름:");
        JTextField nameField = new JTextField();
        JLabel linkLabel = new JLabel("링크:");
        JTextField linkField = new JTextField();

        JButton addButton = new JButton("추가");
        addButton.addActionListener(e -> {
            String foodType = foodTypeField.getText().trim();
            String restaurantName = nameField.getText().trim();
            String link = linkField.getText().trim();

            if (!foodType.isEmpty() && !restaurantName.isEmpty() && !link.isEmpty()) {
                restaurantMap.putIfAbsent(foodType, new ArrayList<>());

                boolean isDuplicate = restaurantMap.get(foodType).stream()
                        .anyMatch(entry -> entry[0].equals(restaurantName) && entry[1].equals(link));
                if (!isDuplicate) {
                    restaurantMap.get(foodType).add(new String[]{restaurantName, link});
                    saveRestaurantsToCSV(FILE_NAME, foodType, restaurantName, link);
                    JOptionPane.showMessageDialog(addFrame, "음식점이 추가되었습니다!");
                    foodTypeField.setText("");
                    nameField.setText("");
                    linkField.setText("");
                } else {
                    JOptionPane.showMessageDialog(addFrame, "이미 존재하는 음식점입니다!", "경고", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(addFrame, "모든 필드를 입력해주세요!", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(foodTypeLabel);
        panel.add(foodTypeField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(linkLabel);
        panel.add(linkField);
        panel.add(new JLabel()); // 빈 자리
        panel.add(addButton);

        addFrame.add(panel);
        addFrame.setVisible(true);
    }

    /**
     * CSV 파일에서 음식점 데이터를 로드합니다.
     *
     * @param fileName CSV 파일 경로.
     */
    private static void loadRestaurantsFromCSV(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("CSV 파일 생성 오류: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    String foodType = values[0];
                    String restaurantName = values[1];
                    String link = values[2];
                    restaurantMap.putIfAbsent(foodType, new ArrayList<>());
                    restaurantMap.get(foodType).add(new String[]{restaurantName, link});
                }
            }
        } catch (IOException e) {
            System.out.println("CSV 파일 읽기 오류: " + e.getMessage());
        }
    }

    /**
     * 새로운 음식점 데이터를 CSV 파일에 추가합니다.
     *
     * @param fileName       CSV 파일 경로.
     * @param foodType       음식 종류.
     * @param restaurantName 음식점 이름.
     * @param link           음식점 링크.
     */
    private static void saveRestaurantsToCSV(String fileName, String foodType, String restaurantName, String link) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(foodType + "," + restaurantName + "," + link);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("CSV 파일 저장 오류: " + e.getMessage());
        }
    }

    /**
     * 음식 종류 목록을 반환합니다.
     *
     * @return 음식 종류 배열.
     */
    private static String[] getFoodTypes() {
        return restaurantMap.keySet().toArray(new String[0]);
    }
}