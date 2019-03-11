package ma.othmane.tgv


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
@ConfigurationProperties("lgv.config")
class LGVConfiguration {

    lateinit var canal: String

    lateinit var destinationFolder: String

    lateinit var dataFolder: String

    lateinit var js:String

    lateinit var p0:BigDecimal
}