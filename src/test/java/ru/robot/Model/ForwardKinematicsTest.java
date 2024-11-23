package ru.robot.Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import ru.robot.Model.DS.Base.RMatrix;
import ru.robot.Model.DS.Vector6;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.robot.Environment.Global.*;
import static ru.robot.Model.ForwardKinematics.FKinBody;
import static ru.robot.Model.Utils.Utils.minus;

public class ForwardKinematicsTest {
    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeEach
    void init(TestInfo testInfo) {
        LOGGER.info(testInfo.getDisplayName());
    }

    /**
     * Computes forward kinematics in the body frame for an open chain robot
     * M The home configuration (position and orientation) of the end-
     *               effector
     * Blist The joint screw axes in the end-effector frame when the
     *                   manipulator is at the home position, in the format of a
     *                   matrix with axes as the columns
     * thetalist A list of joint coordinates
     * A homogeneous transformation matrix representing the end-
     *              effector frame when the joints are at the specified coordinates
     *              (i.t.o Body Frame)

     * <p>Example Input <br>M:
     *         <br>[-1, 0,  0, 0],
     *         <br>[ 0, 1,  0, 6],
     *         <br>[ 0, 0, -1, 2],
     *         <br>[ 0, 0,  0, 1]
     *         list = np.array([[0, 0, -1, 2, 0,   0],
     *      *                           [0, 0,  0, 0, 1,   0],
     *      *                           [0, 0,  1, 0, 0, 0.1]]).T
     *         <br>thetalist
     *         <br>pi / 2.0, 3, pi
     * <p>Output:
     *         np.array([[0, 1,  0,         -5],
     *                   [1, 0,  0,          4],
     *                   [0, 0, -1, 1.68584073],
     *                   [0, 0,  0,          1]])
     */
    @Test
    public void FKinBodyTest(){
        var M = new RMatrix(Arrays.asList(
                minus(ONE), ZERO,ZERO,ZERO,
                ZERO, ONE, ZERO,SIX,
                ZERO,ZERO,minus(ONE),TWO,
                ZERO,ZERO,ZERO,ONE));

        var link1 = new Vector6(ZERO,ZERO,minus(ONE), TWO, ZERO, ZERO);
        var link2 = new Vector6(ZERO,ZERO,ZERO, ZERO, ONE, ZERO);
        var link3 = new Vector6(ZERO,ZERO,ONE, ZERO, ZERO, ONE);
        var Blist = Arrays.asList(link1, link2, link3);

        var thetalist = Arrays.asList(PI.divide(TWO, MC6), THREE, PI);

        var expected = new RMatrix(Arrays.asList(
                ZERO, ONE,ZERO,minus(FIVE),
                ONE, ZERO, ZERO,FOUR,
                ZERO,ZERO,minus(ONE),new BigDecimal("1.685840"),
                ZERO,ZERO,ZERO,ONE));
        var actual  = FKinBody(M, Blist, thetalist);

        LOGGER.debug("FKinBodyTest actual \n'{}", actual.getData());
        LOGGER.debug("FKinBodyTest expected \n'{}", expected.getData());
        for(int i = 0; i < actual.getRowCount(); i++){
            for(int j = 0; j < actual.getColumnCount(); j++){
                assertEquals(expected.getDouble(i,j), actual.getDouble(i,j));
            }
        }

    }
}
