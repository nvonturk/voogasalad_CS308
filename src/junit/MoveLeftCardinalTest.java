package junit;

import org.easymock.EasyMock;
import org.junit.*;
import static org.junit.Assert.*;
import com.syntacticsugar.vooga.gameplayer.attribute.control.actions.movement.IMover;
import com.syntacticsugar.vooga.gameplayer.attribute.control.actions.movement.algs.MoveLeftCardinal;

/**
 * The class <code>MoveLeftCardinalTest</code> contains tests for the class <code>{@link MoveLeftCardinal}</code>.
 *
 * @generatedBy CodePro at 11/28/15 12:56 AM
 * @author Henry
 * @version $Revision: 1.0 $
 */
public class MoveLeftCardinalTest {
	/**
	 * Run the MoveLeftCardinal() constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 11/28/15 12:56 AM
	 */
	@Test
	public void testMoveLeftCardinal_1()
		throws Exception {

		MoveLeftCardinal result = new MoveLeftCardinal();

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the void setMovement(IMover) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 11/28/15 12:56 AM
	 */
	@Test
	public void testSetMovement_1()
		throws Exception {
		MoveLeftCardinal fixture = new MoveLeftCardinal();
		IMover mover = EasyMock.createMock(IMover.class);
		// add mock object expectations here

		EasyMock.replay(mover);

		fixture.setMovement(mover);

		// add additional test code here
		EasyMock.verify(mover);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 11/28/15 12:56 AM
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 * @generatedBy CodePro at 11/28/15 12:56 AM
	 */
	@After
	public void tearDown()
		throws Exception {
		// Add additional tear down code here
	}

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 11/28/15 12:56 AM
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(MoveLeftCardinalTest.class);
	}
}