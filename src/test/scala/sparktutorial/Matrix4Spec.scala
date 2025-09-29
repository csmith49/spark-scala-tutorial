import org.scalatest.funspec.AnyFunSpec
import java.io.{ByteArrayOutputStream, OutputStream, PrintStream}
import util.FileUtil

// Run in local mode and local data.
class Matrix4Spec extends AnyFunSpec {

  describe ("Matrix4") {
    it ("computes the sums of the rows in parallel.") {
      val out     = "output/matrix-math"
      val golden  = "golden/matrix-math/part-00000"
      FileUtil.rmrf(out)  // Delete previous runs, if necessary.

      Matrix4.main(Array("--master", "local", "--quiet", "--dims", "5x10", "--out", out))

      TestUtil.verifyAndClean(s"$out/part-00000", golden, out)
    }
  }
}