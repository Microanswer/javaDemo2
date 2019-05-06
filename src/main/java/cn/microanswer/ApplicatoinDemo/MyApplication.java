package cn.microanswer.ApplicatoinDemo;
/*
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MyApplication extends Application {

    /**
     * 使用 JavaFX 的 API 构建的 二维码生成器。
     *
     * 左右布局，
     * 左边显示输入框，生成按钮，保存按钮。
     * 右边显示二维码结果。
     *
     *
     * @param primaryStage
     * @throws Exception
     *
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("二维码生成器 JavaFx -Microanswer");
        primaryStage.setWidth(500);
        primaryStage.setHeight(300);
        // primaryStage.setResizable(false);

        // 主布局
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(0D);
        gridPane.setVgap(0D);
        gridPane.setPrefWidth(primaryStage.getWidth());


        // 左边
        FlowPane leftPanel = new FlowPane(Orientation.VERTICAL);
        leftPanel.setAlignment(Pos.CENTER);
        ObservableList<Node> children = leftPanel.getChildren();
        leftPanel.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.HOTPINK,
                                null,
                                null
                        )
                )
        );

        children.add(new Button("生成二维码"));
        children.add(new Button("保存"));


        // 右边
        StackPane rightPanel = new StackPane();
        rightPanel.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.GRAY,
                                new CornerRadii(0),
                                null
                        )
                )
        );
        ObservableList<Node> children1 = rightPanel.getChildren();
        children1.add(new ImageView("https://www.baidu.com/img/baidu_jgylogo3.gif"));


        gridPane.add(leftPanel, 0, 0);
        gridPane.add(rightPanel, 1, 0);

        Scene scene = new Scene(gridPane);
        scene.setFill(Color.RED);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
*/