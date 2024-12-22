import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1단계: 음식 종류 선택
        System.out.println("음식 종류를 선택하세요:");
        System.out.println("1. 한식");
        System.out.println("2. 양식");
        System.out.println("3. 중식");
        System.out.println("4. 일식");

        int foodChoice = scanner.nextInt();
        String foodType = "";

        switch (foodChoice) {
            case 1: foodType = "한식"; break;
            case 2: foodType = "양식"; break;
            case 3: foodType = "중식"; break;
            case 4: foodType = "일식"; break;
            default:
                System.out.println("잘못된 입력입니다. 프로그램을 종료합니다.");
                scanner.close();
                return;
        }

        // 2단계: 세부 카테고리 선택
        System.out.println(foodType + "을(를) 선택하셨습니다. 다음 중 카테고리를 선택하세요:");
        System.out.println("1. 면");
        System.out.println("2. 밥");
        System.out.println("3. 빵");

        int categoryChoice = scanner.nextInt();
        String categoryType = "";

        switch (categoryChoice) {
            case 1: categoryType = "면"; break;
            case 2: categoryType = "밥"; break;
            case 3: categoryType = "빵"; break;
            default:
                System.out.println("잘못된 입력입니다. 프로그램을 종료합니다.");
                scanner.close();
                return;
        }

        // 3단계: 추천 메시지 출력
        System.out.println("추천드리는 " + foodType + " 음식점 카테고리는 '" + categoryType + "' 입니다!");
        System.out.println("주변의 음식점을 검색해보세요 :)");

        scanner.close();
    }
}
