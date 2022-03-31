package sk.dualnexon.dualgraph.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import sk.dualnexon.dualgraph.lib.algorithm.parent.Algorithm;

public class VisualControl implements Updatable {
	
	private static final String LABEL_BACK = "Previous step";
	private static final String LABEL_FORWARD = "Next step";
	private static final String LABEL_DEFAULT_TEXT = "0/0";
	private static final double BTN_MARGIN = 10;
	
	private Algorithm algorithm;
	
	private HBox box;
	private Label labelIteration;
	
	public VisualControl(Algorithm algorithm) {
		this.algorithm = algorithm;
		createControls();
	}
	
	private void createControls() {
		
		Button btnDestroy = new Button("X");
		btnDestroy.setOnAction(e-> {
			algorithm.getGraph().getWorkspace().destroyCurrentAlgorithm();
		});
		
		Button btnBack = new Button(LABEL_BACK);
		btnBack.setOnAction(e-> {
			algorithm.getVisualizer().previousStep();
			update();
		});
		
		Button btnForward = new Button(LABEL_FORWARD);
		btnForward.setOnAction(e-> {
			algorithm.getVisualizer().nextStep();
			update();
		});
		
		labelIteration = new Label(LABEL_DEFAULT_TEXT);
		labelIteration.setFont(new Font(32));
		
		box = new HBox(BTN_MARGIN);
		box.setVisible(false);
		HBox.setMargin(btnDestroy, new Insets(BTN_MARGIN));
		HBox.setMargin(btnBack, new Insets(BTN_MARGIN));
		HBox.setMargin(btnForward, new Insets(BTN_MARGIN));
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(btnDestroy, btnBack, labelIteration, btnForward);
		algorithm.getGraph().getWorkspace().addNode(box);
		
	}
	
	public void setVisible(boolean value) {
		box.setVisible(value);
	}
	
	@Override
	public void update() {
		int inc = (algorithm.getVisualizer().getMaskCount() > 0 ? 1 : 0);
		labelIteration.setText(algorithm.getVisualizer().getCurrentMask()+inc + "/" + algorithm.getVisualizer().getMasks().size());
	}
	
	@Override
	public void destroy() {
		algorithm.getGraph().getWorkspace().removeNode(box);
	}
	
}
