package junit;

import java.io.File;
import org.junit.*;
import authoring.library.TowerLibrary;
import static org.junit.Assert.*;

/**
 * The class <code>TowerLibraryTest</code> contains tests for the class <code>{@link TowerLibrary}</code>.
 *
 * @generatedBy CodePro at 11/28/15 12:57 AM
 * @author Henry
 * @version $Revision: 1.0 $
 */
public class TowerLibraryTest {
	/**
	 * Run the TowerLibrary(File) constructor test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 11/28/15 12:57 AM
	 */
	@Test
	public void testTowerLibrary_1()
		throws Exception {
		File XMLDirectory = new File("");

		TowerLibrary result = new TowerLibrary(XMLDirectory);

		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.Error: Unresolved compilation problem: 
		//       The method showImageOptionsFromXML(File) is undefined for the type IconPane
		//       
		//       at authoring.library.AbstractObjectLibrary.populateOptionIcons(AbstractObjectLibrary.java:30)
		//       at authoring.library.AbstractObjectLibrary.<init>(AbstractObjectLibrary.java:16)
		//       at authoring.library.TowerLibrary.<init>(TowerLibrary.java:8)
		assertNotNull(result);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 11/28/15 12:57 AM
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
	 * @generatedBy CodePro at 11/28/15 12:57 AM
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
	 * @generatedBy CodePro at 11/28/15 12:57 AM
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(TowerLibraryTest.class);
	}
}