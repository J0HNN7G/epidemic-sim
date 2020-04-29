package org.epi.model2;

import org.epi.model.StatusType;
import org.epi.util.Error;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/** A simple model of a human.
 * The main actors in the simulator.*/
public class Human {

    /** The location of this human.*/
    private final Property<Location> location;

    /** The graphical view of this human.*/
    private final Model model;

    /** The health status of this human.*/
    private StatusType status;

    /** The immune system protecting this human.*/
    private final ImmuneSystem immuneSystem;

    /** The pathogen infecting this human.*/
    private Pathogen pathogen;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a human at a given location
     *
     * @param behaviour the behaviour of this human
     * @param location the location of this human
     * @throws NullPointerException if the given parameter is null
     */
    public Human(Location location, Behaviour behaviour) {
        Objects.requireNonNull(behaviour, Error.getNullMsg("behaviour"));
        Objects.requireNonNull(location, Error.getNullMsg("location"));

        this.model = new Model(this, behaviour);

        this.status = StatusType.HEALTHY;

        this.location = new SimpleObjectProperty<>(location);
        location.getPopulation().add(this);

        this.immuneSystem = new ImmuneSystem(this);

        initEvents();
    }

    /**
     * Initialise all event listeners.
     */
    private void initEvents() {
        // If the location of the human is changed, they switch population.
        location.addListener((observable, oldValue, newValue) -> {
            oldValue.getPopulation().remove(Human.this);
            newValue.getPopulation().add(Human.this);
        });
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Check if this human is infected with a pathogen.
     *
     * @return true if this human is infected, otherwise false
     */
    private boolean isInfected() {
        return pathogen != null;
    }

    /**
     * Update the health status of this human.
     */
    private void status() {
        if (immuneSystem.isImmune()) {
            status = StatusType.RECOVERED;
        } else if (isInfected()) {
            status = StatusType.INFECTED;
        } else {
            status = StatusType.HEALTHY;
        }
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * All regulatory behaviour for the pathogen which happens in each timer update.
     *
     * @param elapsedSeconds the number of seconds elapsed since the pathogen was last updated
     */
    public void pathogen(double elapsedSeconds) {
        if (isInfected()) {
            pathogen.infect();

            pathogen.live(elapsedSeconds);
        }
    }

    /**
     * All regulatory behaviour for the immune system which happens in each timer update.
     *
     * @param elapsedSeconds the number of seconds elapsed since the immune system was last updated
     */
    public void immuneSystem(double elapsedSeconds) {
        immuneSystem.live(elapsedSeconds);

        if (isInfected()) {
            immuneSystem.defend();
        }
    }

    /**
     * All regulatory behaviour for the model which happens in each timer update.
     *
     * @param elapsedSeconds the number of seconds elapsed since the model was last updated
     */
    public void model(double elapsedSeconds) {
        status();


    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #immuneSystem}.
     *
     * @return {@link #immuneSystem}
     */
    public Location getLocation() {
        return location.getValue();
    }

    /**
     * Getter for {@link #location} property.
     *
     * @return {@link #location} property
     */
    public Property<Location> locationProperty() {
        return location;
    }

    /**
     * Getter for {@link #model}.
     *
     * @return {@link #model}
     */
    public Model getModel() {
        return model;
    }

    /**
     * Getter for {@link #status}
     *
     * @return {@link #status}
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Setter for {@link #status}
     *
     * @param status the status of this human
     * @throws NullPointerException if the given parameter is null
     */
    public void setStatus(StatusType status) {
        Objects.requireNonNull(status,Error.getNullMsg("status"));
        this.status = status;
    }

    /**
     * Getter for {@link #immuneSystem}.
     *
     * @return {@link #immuneSystem}
     */
    public ImmuneSystem getImmuneSystem() {
        return immuneSystem;
    }

    /**
     * Setter for {@link #pathogen}.
     */
    public void setPathogen(Pathogen pathogen) {
        this.pathogen = pathogen;
    }

    /**
     * Getter for {@link #pathogen}.
     *
     * @return {@link #pathogen}
     */
    public Pathogen getPathogen() {
        return pathogen;
    }

}
