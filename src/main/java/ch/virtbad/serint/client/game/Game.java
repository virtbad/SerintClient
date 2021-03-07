package ch.virtbad.serint.client.game;

import ch.virtbad.serint.client.networking.Communications;

/**
 * @author Virt
 */
public class Game {

    private Communications communications;

    public Game(Communications communications){
        this.communications = communications;
        communications.setGame(this);
    }

}
