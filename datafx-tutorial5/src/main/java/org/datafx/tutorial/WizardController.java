package org.datafx.tutorial;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.Flow;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.FlowHandler;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.container.AnimatedFlowContainer;
import org.datafx.controller.flow.container.ContainerAnimations;
import org.datafx.controller.util.VetoException;

import javax.annotation.PostConstruct;

/**
 * This class defines the main controller of the wizard. The complete action toolbar is managed here. In addition a
 * flow that contains all the custom views of the wizard is added to the view. The navigation for this internal flow
 * is managed here.
 */
@FXMLController("wizard.fxml")
public class WizardController {

    @FXML
    @ActionTrigger("back")
    private Button backButton;
    @FXML
    @ActionTrigger("finish")
    private Button finishButton;
    @FXML
    @ActionTrigger("next")
    private Button nextButton;

    @FXML
    private StackPane centerPane;

    private FlowHandler flowHandler;

    /**
     * The {@code init} method defines a internal flow that contains the steps of the wizard as separate views.
     * This internal flow will use animations for the navigation between different views.
     * @throws FlowException if the internal flow can't be created
     */
    @PostConstruct
    public void init() throws FlowException {
        Flow flow = new Flow(WizardView1Controller.class).
                withLink(WizardView1Controller.class, "next", WizardView2Controller.class).
                withLink(WizardView2Controller.class, "next", WizardView3Controller.class).
                withLink(WizardView3Controller.class, "next", WizardView4Controller.class).
                withLink(WizardView4Controller.class, "next", WizardView5Controller.class);

        flowHandler = flow.createHandler();
        centerPane.getChildren().add(flowHandler.start(new AnimatedFlowContainer(Duration.millis(320), ContainerAnimations.ZOOM_IN)));

        backButton.setDisable(true);
    }

    /**
     * This method will be called when the {@code back} action will be executed. The method handles the navigation of
     * the internal flow that contains the steps of the wizard as separate views. In addition the states of the action
     * buttons will be managed.
     * @throws VetoException If the navigation can't be executed
     * @throws FlowException If the navigation can't be executed
     */
    @ActionMethod("back")
    public void onBack() throws VetoException, FlowException {
        flowHandler.navigateBack();
        if(flowHandler.getCurrentViewControllerClass().equals(WizardView1Controller.class)) {
            backButton.setDisable(true);
        } else {
            backButton.setDisable(false);
        }
        finishButton.setDisable(false);
        nextButton.setDisable(false);
    }

    /**
     * This method will be called when the {@code next} action will be executed. The method handles the navigation of
     * the internal flow that contains the steps of the wizard as separate views. In addition the states of the action
     * buttons will be managed.
     * @throws VetoException If the navigation can't be executed
     * @throws FlowException If the navigation can't be executed
     */
    @ActionMethod("next")
    public void onNext() throws VetoException, FlowException {
        flowHandler.handle("next");
        if(flowHandler.getCurrentViewControllerClass().equals(WizardView5Controller.class)) {
            nextButton.setDisable(true);
            finishButton.setDisable(true);
        } else {
            nextButton.setDisable(false);
        }
        backButton.setDisable(false);
    }

    /**
     * This method will be called when the {@code finish} action will be executed. The method handles the navigation of
     * the internal flow that contains the steps of the wizard as separate views. In addition the states of the action
     * buttons will be managed.
     * @throws VetoException If the navigation can't be executed
     * @throws FlowException If the navigation can't be executed
     */
    @ActionMethod("finish")
    public void onFinish() throws VetoException, FlowException {
        flowHandler.navigateTo(WizardView5Controller.class);
        finishButton.setDisable(true);
        nextButton.setDisable(true);
        backButton.setDisable(false);
    }
}
