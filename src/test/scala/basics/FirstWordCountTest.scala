package basics

import org.scalatest.funspec.AnyFunSpec
import utils.SparkTestSessionWrapper

class FirstWordCountSpec extends AnyFunSpec with SparkTestSessionWrapper {
    describe("basics.FirstWordCountTest") {
        it("should return a numbers dataframe with 3 rows") {
            val numbersDf = FirstDataFrame.getNumbersDf()
            assert(numbersDf.count() == 3)
            numbersDf.schema.fields.foreach(f => {
                assert(f.name == "word" || f.name == "number")
            })
        }

        it ("should return dataframe with 2 columns 'word' and 'number'") {
            val numbersDf = FirstDataFrame.getNumbersDf()
            assert(numbersDf.columns.length == 2)
        }
    }

}
