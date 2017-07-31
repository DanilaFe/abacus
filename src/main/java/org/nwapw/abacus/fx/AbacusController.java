package org.nwapw.abacus.fx;

import javafx.fxml.FXML;
import org.nwapw.abacus.Abacus;

public class AbacusController {

    private Abacus abacus;

    @FXML
    public void initialize(){
        abacus = new Abacus();
    }

}
