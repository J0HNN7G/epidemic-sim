package org.epi;

import org.epi.model.BehaviourDistribution;
import org.epi.model.Pathogen;
import org.epi.model.Simulator;
import org.epi.model.World;
import org.epi.view.RootLayoutController;
import org.epi.view.SimulatorController;

import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/** Main class for the application.
 *
 * Everything is initialised and run from this class.*/
public class MainApp extends Application {

    /** Minimum width of the application window in pixels.*/
    private static final double MIN_WIDTH = 555;
    /** Minimum height of the application window in pixels.*/
    private static final double MIN_HEIGHT = 678;
    /** Minimum width of the application window in pixels.*/
    private static final double MAX_WIDTH = 1100;
    /** Maximum height of the application window in pixels.*/
    private static final double MAX_HEIGHT = 740;
    /** Maximum width of the application window in pixels.*/
    private static final double PREF_WIDTH = 963;
    /** Maximum height of the application window in pixels.*/
    private static final double PREF_HEIGHT = MIN_HEIGHT;

    /** The main container for the application.*/
    private Stage primaryStage;

    /** The root layout for the application.*/
    private BorderPane rootLayout;

    /** Simulator currently being showed.*/
    private Simulator simulator;

    private World world;

    private BehaviourDistribution behaviourDistribution;

    private Pathogen pathogen;

    /**
     * Constructor for the main application.
     */
    public MainApp() {
        // Added a sample simulation
        World world = new World(500, 20,0.5,10);
        BehaviourDistribution dist = new BehaviourDistribution(1,0, 0);
        Pathogen pathogen = new Pathogen(5,0.05,0.5,0.8,12);

        simulator = new Simulator(world, dist, pathogen);
    }

    /**
     * Standard Java main method; used to launch the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Epi");

        // Set the application icon.
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Icon.png").toExternalForm()));

        //Set size specifications.
        this.primaryStage.setMinWidth(MIN_WIDTH);
        this.primaryStage.setMinHeight(MIN_HEIGHT);
        this.primaryStage.setMaxWidth(MAX_WIDTH);
        this.primaryStage.setMaxHeight(MAX_HEIGHT);
        this.primaryStage.setWidth(PREF_WIDTH);
        this.primaryStage.setHeight(PREF_HEIGHT);

        initRootLayout();
        showSimulator();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load the root layout from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the simulator inside the root layout.
     */
    public void showSimulator() {
        try {
            // Load the simulator from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Simulator.fxml"));
            AnchorPane simulator = loader.load();

            // Set the simulator into the center of root layout.
            rootLayout.setCenter(simulator);

            // Give the controller access to the main app.
            SimulatorController controller = loader.getController();
            controller.setMainApp(this);
            controller.showSimulation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #simulator}.
     *
     * @return {@link #simulator}
     */
    public Simulator getSimulator() {
        return simulator;
    }

    /**
     * Setter for {@link #simulator}.
     *
     * @param simulator {@link #simulator}
     * @throws NullPointerException if the given parameter is null
     */
    public void setSimulator(Simulator simulator) {
        Objects.requireNonNull(simulator);
        this.simulator = simulator;
    }

}