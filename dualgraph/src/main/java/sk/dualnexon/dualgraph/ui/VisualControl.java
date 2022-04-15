package sk.dualnexon.dualgraph.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class VisualControl implements Updatable {
	
	private static final String LABEL_BACK = "Previous step";
	private static final String LABEL_FORWARD = "Next step";
	private static final String LABEL_FIRST = "To first step";
	private static final String LABEL_LAST = "To last step";
	private static final String LABEL_DEFAULT_TEXT = "0/0";
	private static final double BTN_MARGIN = 10;
	
	private Algorithm algorithm;
	
	private VBox boxControl;
	private HBox boxTop, boxButtons, boxMessage;
	private Label labelIteration;
	private Label labelName;
	private Label labelMessage;
	
	public VisualControl(Algorithm algorithm) {
		this.algorithm = algorithm;
		createControls();
	}
	
	private void createControls() {
		
		boxControl = new VBox();
		boxControl.setVisible(false);
		
		labelName = new Label(algorithm.getName());
		labelName.setFont(new Font(22));
		
		boxTop = new HBox(BTN_MARGIN);
		boxTop.getChildren().addAll(labelName);
		
		Button btnDestroy = new Button("X");
		btnDestroy.setOnAction(e -> {
			algorithm.getGraph().getWorkspace().destroyCurrentAlgorithm();
		});
		
		Button btnFirst = new Button(LABEL_FIRST);
		btnFirst.setOnAction(e -> {
			algorithm.getVisualizer().applyMask(0);
			update();
		});
		
		Button btnBack = new Button(LABEL_BACK);
		btnBack.setOnAction(e -> {
			algorithm.getVisualizer().previousStep();
			update();
		});
		
		Button btnForward = new Button(LABEL_FORWARD);
		btnForward.setOnAction(e -> {
			algorithm.getVisualizer().nextStep();
			update();
		});
		
		Button btnLast = new Button(LABEL_LAST);
		btnLast.setOnAction(e -> {
			algorithm.getVisualizer().applyLastMask();
			update();
		});
		
		labelIteration = new Label(LABEL_DEFAULT_TEXT);
		labelIteration.setFont(new Font(32));
		
		boxButtons = new HBox(BTN_MARGIN);
		HBox.setMargin(btnDestroy, new Insets(BTN_MARGIN));
		HBox.setMargin(btnBack, new Insets(BTN_MARGIN));
		HBox.setMargin(btnForward, new Insets(BTN_MARGIN));
		HBox.setMargin(btnFirst, new Insets(BTN_MARGIN));
		HBox.setMargin(btnLast, new Insets(BTN_MARGIN));
		boxButtons.setAlignment(Pos.CENTER);
		boxButtons.getChildren().addAll(btnDestroy, btnFirst, btnBack, labelIteration, btnForward, btnLast);
		
		labelMessage = new Label();
		labelMessage.setFont(new Font(18));
		
		boxMessage = new HBox();
		boxMessage.getChildren().add(labelMessage);
		
		boxControl.getChildren().addAll(boxTop, boxButtons, boxMessage);
		algorithm.getGraph().getWorkspace().addNode(boxControl);
		
	}
	
	public void setVisible(boolean value) {
		boxControl.setVisible(value);
	}
	
	@Override
	public void update() {
		int inc = (algorithm.getVisualizer().getMaskCount() > 0 ? 1 : 0);
		labelIteration.setText(algorithm.getVisualizer().getCurrentMask()+inc + "/" + algorithm.getVisualizer().getMasks().size());
		labelName.setText(algorithm.getName());
		labelMessage.setText(algorithm.getVisualizer().getMasks().get(algorithm.getVisualizer().getCurrentMask()).getMessage());
		boxControl.toFront();
	}
	
	@Override
	public void destroy() {
		algorithm.getGraph().getWorkspace().removeNode(boxControl);
	}
	
}
