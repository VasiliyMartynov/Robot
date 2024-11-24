package ru.robot.Model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.robot.Model.DS.Base.RMatrix;

import static ru.robot.Model.DS.Base.RMatrix.mult;
import static ru.robot.Model.Motion.*;
import ru.robot.Model.DS.Vector6;

import java.math.BigDecimal;
import java.util.List;



public class ForwardKinematics {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Computes forward kinematics in the body frame for an open chain robot
     * @param M The home configuration (position and orientation) of the end-
     *               effector
     * @param Blist The joint screw axes in the end-effector frame when the
     *                   manipulator is at the home position, in the format of a
     *                   matrix with axes as the columns
     * @param thetalist A list of joint coordinates
     * @return A homogeneous transformation matrix representing the end-
     *              effector frame when the joints are at the specified coordinates
     *              (i.t.o Body Frame)

     * <p>Example Input <br>M:
     *         <br>[-1, 0,  0, 0],
     *         <br>[ 0, 1,  0, 6],
     *         <br>[ 0, 0, -1, 2],
     *         <br>[ 0, 0,  0, 1]
     *         Blist = np.array([[0, 0, -1, 2, 0,   0],
     *                           [0, 0,  0, 0, 1,   0],
     *                           [0, 0,  1, 0, 0, 0.1]]).T
     *         <br>pi / 2.0, 3, pi
     * <p>Output:
     *         np.array([[0, 1,  0,         -5],
     *                   [1, 0,  0,          4],
     *                   [0, 0, -1, 1.68584073],
     *                   [0, 0,  0,          1]])
     */
    public static RMatrix FKinBody(RMatrix M, List<Vector6> Blist, List<BigDecimal> thetalist){
//        LOGGER.info("FKinBody has been started");
        if (Blist.size() != thetalist.size()){
            LOGGER.info("Size of Blist and aren't equal");
            LOGGER.info("FKinBody has been finished");
            return new Motion().getData();
        }
        var T = M;
        for(int i = 0; i <= thetalist.size() - 1; i++){
            var z = Vector6.mult(Blist.get(i), thetalist.get(i));
            T = mult(T, MatrixExp6(VecTose3(z)));
        }
        LOGGER.debug("FKinBody result '{}'", T);

        return T;
    }


    /**
     * Computes forward kinematics in the space frame for an open chain robot
     * @param M The home configuration (position and orientation) of the end-
     *               effector
     * @param Slist The joint screw axes in the space frame when the
     *                   manipulator is at the home position, in the format of a
     *                   matrix with axes as the columns
     * @param thetalist A list of joint coordinates
     * @return A homogeneous transformation matrix representing the end-
     *              effector frame when the joints are at the specified coordinates
     *              (i.t.o Space Frame)

     * <p>Example Input <br>M:
     *         <br>[-1, 0,  0, 0],
     *         <br>[ 0, 1,  0, 6],
     *         <br>[ 0, 0, -1, 2],
     *         <br>[ 0, 0,  0, 1]
     *         Blist = np.array([[0, 0,  1,  4, 0,    0],
     *                           [0, 0,  0,  0, 1,    0],
     *                           [0, 0, -1, -6, 0, -0.1]]).T
     *         <br>pi / 2.0, 3, pi
     * <p>Output:
     *         np.array([[0, 1,  0,         -5],
     *                   [1, 0,  0,          4],
     *                   [0, 0, -1, 1.68584073],
     *                   [0, 0,  0,          1]])
     */
    public static RMatrix FKinSpace(RMatrix M, List<Vector6> Slist, List<BigDecimal> thetalist){
//        LOGGER.info("FKinBody has been started");
        if (Slist.size() != thetalist.size()){
            LOGGER.info("Size of Slist and aren't equal");
            LOGGER.info("FKinSpace has been finished");
            return new Motion().getData();
        }
        var T = M;
        for(int i = thetalist.size() - 1; i >= 0; i--){
            var z = Vector6.mult(Slist.get(i), thetalist.get(i));
            T = mult(MatrixExp6(VecTose3(z)), T);
        }
        LOGGER.debug("FKinSpace result '{}'", T);
        return T;
    }
}
