import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 음식 종류 선택
        System.out.println("음식 종류를 선택하세요:");
        System.out.println("1. 한식");
        System.out.println("2. 양식");
        System.out.println("3. 중식");
        System.out.println("4. 일식");
        System.out.println("5. 패스트푸드");
        System.out.println("6. 카페");
        System.out.println("7. 분식");
        System.out.println("8. 야식");
        System.out.println("9. 고기");

        int foodChoice = scanner.nextInt();
        String foodType = "";

        switch (foodChoice) {
            case 1: foodType = "한식"; break;
            case 2: foodType = "양식"; break;
            case 3: foodType = "중식"; break;
            case 4: foodType = "일식"; break;
            case 5: foodType = "패스트푸드"; break;
            case 6: foodType = "카페"; break;
            case 7: foodType = "분식"; break;
            case 8: foodType = "야식"; break;
            case 9: foodType = "고기"; break;
            default:
                System.out.println("잘못된 입력입니다. 프로그램을 종료합니다.");
                scanner.close();
                return;
        }

        // CSV 파일에서 데이터 읽기
        System.out.println("추천드리는 " + foodType + " 음식점:");
        readCSVFile("food/Restaurant.csv", foodType);

        scanner.close();
    }

    // CSV 파일 읽기 메서드
    public static void readCSVFile(String fileName, String foodType) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3 && values[0].equals(foodType)) {
                    System.out.println("- " + values[1] + " (" + values[2] + ")");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("해당 음식 종류에 맞는 음식점이 없습니다.");
            }
        } catch (IOException e) {
            System.out.println("CSV 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
