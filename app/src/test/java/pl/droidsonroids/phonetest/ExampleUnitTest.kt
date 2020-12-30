package pl.droidsonroids.phonetest

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class ExampleUnitTest : ShouldSpec({

    val phoneNumberUtil = PhoneNumberUtil.getInstance()

    val number = phoneNumberUtil.parse("666777888", "PL")

    should("extract country code") {
        number.countryCode shouldBe 48
    }

    should("extract national number") {
        number.nationalNumber shouldBe 666777888L
    }

    should("format E164") {
        phoneNumberUtil.format(number, PhoneNumberFormat.E164) shouldBe "+48666777888"
    }

    should("format international") {
        phoneNumberUtil.format(number, PhoneNumberFormat.INTERNATIONAL) shouldBe "+48 666 777 888"
    }

    should("format national") {
        phoneNumberUtil.format(number, PhoneNumberFormat.NATIONAL) shouldBe "666 777 888"
    }

    should("show example geo number") {
        phoneNumberUtil.getExampleNumber("RO") shouldBe "test"
    }

    should("extract non-geo country code") {
        phoneNumberUtil.parse("011 882 16 31049999", "US").countryCode shouldBe 1
    }
})