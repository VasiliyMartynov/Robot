package ru.robot.Model.DS;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ujmp.core.Matrix;
import ru.robot.Model.DS.Base.RMatrix;
import java.math.BigDecimal;
import static ru.robot.Environment.Global.MC6;
import static ru.robot.Environment.Global.ONE;
import static ru.robot.Model.Utils.Utils.nearZero;
import static ru.robot.Model.DS.Base.RMatrix.*;

public class RotationMatrix {

    private static final Logger LOGGER = LogManager.getLogger();

    RMatrix data;
    int size = 3;

    public RotationMatrix(RMatrix R){
        LOGGER.info("Checks started");
        if (checkIsSO3(R.getData())) {
            LOGGER.info("Check is OK");
            this.data = R;
            this.size = (int) R.getRowCount();
        } else {
            LOGGER.info("Check isn't OK");
            String message = "Body rotation cannot be instantiated, " +
                    "because Matrix isn't R ∈ SO(3), " +
                    "det R not equals 1, please check input values";
            IllegalArgumentException e = new IllegalArgumentException(message);
            LOGGER.debug(message);
            throw e;
        }
    }

    private Boolean checkIsSO3(Matrix R){
        LOGGER.info("checkIsSO start");
        var Rtransponsed = R.transpose();
        var RmultRtansposed = R.mtimes(Rtransponsed);
        var det = BigDecimal.valueOf(R.det()).round(MC6);
        var diff = ONE.subtract(det, MC6);
        LOGGER.debug("diff is `{}`", diff);
        boolean ruleOne = nearZero(diff) < 0;
        LOGGER.debug("rule 1 - determinant = 1: `{}`", ruleOne);

        var I = getIdentityMatrix((int) R.getRowCount()).getData();
        boolean ruleTwo = compareMatrices(I, RmultRtansposed);
        LOGGER.debug("rule 2 - R*Rt = I: `{}`", ruleTwo);
        boolean ruleThree = haveSize(R);
        LOGGER.debug("rule 3 - input matrix has same size as copmared: `{}`", ruleThree);
        LOGGER.info("checkIsSO finished with result:`{}`", ruleOne & ruleTwo & ruleThree );
        return ruleOne & ruleTwo & ruleThree;
    }





    public RMatrix getData(){
        return this.data;
    }
}
