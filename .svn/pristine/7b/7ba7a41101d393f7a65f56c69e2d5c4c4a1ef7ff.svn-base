package utils;

import com.example.hsdj.MenuApplication;

public class AccessKeyUtils {
    public static String getAccessKey(MenuApplication menuApplication, int menuId){
        String accessKey=null;
        if (menuApplication.getMenu_id()==menuId){
            accessKey=menuApplication.getAccessKey();
        }else {
            for (int i = 0; i < menuApplication.getChildren().size(); i++) {
                accessKey  = getAccessKey(menuApplication.getChildren().get(i), menuId);
                if (accessKey!=null){
                    return accessKey;
                }
            }
        }
        return accessKey;
    }
}
