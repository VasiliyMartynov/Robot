package ru.robot.Model.DataStructure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ujmp.core.Matrix;
import ru.robot.Model.DataStructure.Base.RMatrix;
import static ru.robot.Model.CoordinateSystem.Cartesian.Utils.Utils.minus;
import static ru.robot.Model.DataStructure.Base.RMatrix.*;

public class SkewSymmetricMatrix {
    private static final Logger LOGGER = LogManager.getLogger();

    RMatrix data;
    int size = 3;

    public SkewSymmetricMatrix(RMatrix R){

        if (checkIsLittleSO3(R.getData())) {
            this.data = R;
            this.size = (int) R.getRowCount();
        } else {
            String message = "Body rotation cannot be instantiated, " +
                    "because Matrix isn't R ∈ SO(3), " +
                    "det R not equals 1, please check input values";
            IllegalArgumentException e = new IllegalArgumentException(message);
            LOGGER.debug(message);
            throw e;
        }
    }

    private Boolean checkIsLittleSO3(Matrix R){
        LOGGER.debug("checkIsLittleSO3 start");
        boolean ruleOne = R.trace() == 0;
        LOGGER.debug("rule 1 - trace is ZERO: `{}`", ruleOne);
        boolean ruleTwo =
                R.getAsBigDecimal(0,1).compareTo(minus(R.getAsBigDecimal(1,0))) == 0 &
                R.getAsBigDecimal(0,2).compareTo(minus(R.getAsBigDecimal(2,0))) == 0 &
                R.getAsBigDecimal(1,2).compareTo(minus(R.getAsBigDecimal(2,1))) == 0;
        LOGGER.debug("rule 2 - R*Rt = I: `{}`", ruleTwo);
        boolean ruleThree = haveSize(R);
        LOGGER.debug("rule 3 - input matrix has same size as compared: `{}`", ruleThree);
        LOGGER.debug("checkIsLittleSO3 finished with result:`{}`", ruleOne & ruleTwo & ruleThree );
        return ruleOne & ruleTwo & ruleThree;
    }

    public RMatrix getData(){
        return this.data;
    }
}