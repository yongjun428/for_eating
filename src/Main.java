import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author yongjun428 (김용준, 2021012011)
 *
 *
 * @created 2024-12-22
 *
 * <ul>
 *     <li> 2024-12-22 : 기본틀 을 생성  csv를 사용하여 한식,중식,일식,양식으로 음식종류를 구별하고
 *          카테고리를 사용하여 면,밥,빵 으로 다양하게 고를 수 있게 만듬
 *     </li>
 *     <li> 2024-12-23 : csv에 링크를 추가하여 가게에 대한 더 많은 정보를 알 수 있게함  </li>
 *     <li>
 *         2024-12-23 : .카테고리를 없애고"한식,양식,중식,일식, 패스트푸드, 카페, 분식, 야식, 고기" 를 추가
 *     </li>
 *     <li>
 *         2024-12-23 : JFrame 을 사용하여 패널, 버튼, 콤보박스 기능 추가
 *     </li>
 * </ul>
 */

public class Main extends JFrame {
    private static final String FILE_NAME = "food/Restaurant.csv";

    public Main() {
        setTitle("음식점 관리 프로그램");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 메인 패널
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        // 버튼 생성
        JButton viewButton = new JButton("음식점 조회");
        JButton addButton = new JButton("음식점 추가");
        JButton exitButton = new JButton("종료");

        // 버튼 이벤트 연결
        viewButton.addActionListener(e -> openViewRestaurantsWindow());
        addButton.addActionListener(e -> openAddRestaurantWindow());
        exitButton.addActionListener(e -> System.exit(0));

        // 패널에 버튼 추가
        panel.add(viewButton);
        panel.add(addButton);
        panel.add(exitButton);

        add(panel);

        setVisible(true);
    }

    private void openViewRestaurantsWindow() {
        JFrame viewFrame = new JFrame("음식점 조회");
        viewFrame.setSize(400, 400);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 음식 종류 선택 콤보 박스
        String[] foodTypes = {"한식", "양식", "중식", "일식", "패스트푸드", "카페", "분식", "야식", "고기"};
        JComboBox<String> foodTypeComboBox = new JComboBox<>(foodTypes);

        // 결과 표시 텍스트 영역
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // 조회 버튼
        JButton searchButton = new JButton("조회");
        searchButton.addActionListener(e -> {
            String selectedFoodType = (String) foodTypeComboBox.getSelectedItem();
            String results = readRestaurants(selectedFoodType);
            resultArea.setText(results);
        });

        // 패널 구성
        panel.add(foodTypeComboBox, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(searchButton, BorderLayout.SOUTH);

        viewFrame.add(panel);
        viewFrame.setVisible(true);
    }

    private void openAddRestaurantWindow() {
        JFrame addFrame = new JFrame("음식점 추가");
        addFrame.setSize(400, 300);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // 입력 필드
        JLabel foodTypeLabel = new JLabel("음식 종류:");
        JTextField foodTypeField = new JTextField();
        JLabel nameLabel = new JLabel("음식점 이름:");
        JTextField nameField = new JTextField();
        JLabel linkLabel = new JLabel("링크:");
        JTextField linkField = new JTextField();

        // 추가 버튼
        JButton addButton = new JButton("추가");
        addButton.addActionListener(e -> {
            String foodType = foodTypeField.getText();
            String name = nameField.getText();
            String link = linkField.getText();
            if (!foodType.isEmpty() && !name.isEmpty() && !link.isEmpty()) {
                addRestaurant(foodType, name, link);
                JOptionPane.showMessageDialog(addFrame, "음식점 정보가 추가되었습니다!");
                foodTypeField.setText("");
                nameField.setText("");
                linkField.setText("");
            } else {
                JOptionPane.showMessageDialog(addFrame, "모든 필드를 채워주세요!");
            }
        });

        // 패널 구성
        panel.add(foodTypeLabel);
        panel.add(foodTypeField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(linkLabel);
        panel.add(linkField);
        panel.add(new JLabel()); // 빈 자리 채우기
        panel.add(addButton);

        addFrame.add(panel);
        addFrame.setVisible(true);
    }

    private String readRestaurants(String foodType) {
        StringBuilder results = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3 && values[0].equals(foodType)) {
                    results.append("- ").append(values[1]).append(" (").append(values[2]).append(")\n");
                }
            }
        } catch (IOException e) {
            results.append("CSV 파일을 읽는 중 오류가 발생했습니다: ").append(e.getMessage());
        }

        if (results.length() == 0) {
            results.append("해당 음식 종류에 맞는 음식점이 없습니다.");
        }

        return results.toString();
    }

    private void addRestaurant(String foodType, String name, String link) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(foodType + "," + name + "," + link);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "파일에 정보를 추가하는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
