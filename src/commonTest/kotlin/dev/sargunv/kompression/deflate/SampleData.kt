package dev.sargunv.kompression.deflate

import kotlin.io.encoding.Base64

data class SampleData(val original: String, private val deflatedBase64: String) {
  val deflated: ByteArray = Base64.decode(deflatedBase64)

  companion object {
    val empty = SampleData(original = "", deflatedBase64 = "eF4DAAAAAAE=")
    val quickBrownFox =
      SampleData(
        original = "The quick brown fox jumps over the lazy dog",
        deflatedBase64 = "eF4LyUhVKCzNTM5WSCrKL89TSMuvUMgqzS0oVsgvSy1SKAFK5yRWVSqk5KcDAFvcD9o=",
      )
    val loremIpsum =
      SampleData(
        original =
          "\n\n" +
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis mauris vel accumsan placerat. Sed blandit ac nisi non fermentum. Pellentesque maximus nec metus sed commodo. In sit amet aliquet quam. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed hendrerit venenatis velit ac tristique. Nullam gravida vestibulum neque non vehicula.\n" +
            "\n" +
            "Sed molestie pretium leo, in suscipit augue pretium ut. Fusce ligula odio, elementum eu mi quis, mollis euismod turpis. Nulla commodo lacus turpis, et laoreet est volutpat sit amet. Etiam tristique nibh massa, sit amet volutpat turpis hendrerit non. Nulla est neque, sollicitudin ut dui euismod, blandit fringilla neque. Maecenas dapibus pharetra porttitor. Vivamus massa purus, luctus in vulputate at, ullamcorper nec libero. Fusce et sem leo. Morbi a libero justo. Praesent id lacinia lorem. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas et dapibus ex, ornare elementum libero. Integer tristique, nisl eget maximus molestie, justo mi fermentum eros, id hendrerit arcu nulla sed magna. Nam aliquet nisi id quam egestas consequat. Curabitur vel enim eros. Phasellus ac est posuere, porta turpis et, ultrices enim.\n" +
            "\n" +
            "In eu tempor purus. Nam ac faucibus quam, ultricies malesuada massa. Etiam vitae enim quis erat hendrerit malesuada non in massa. Quisque consequat, tellus non dictum tincidunt, nisl leo malesuada augue, non porttitor leo magna quis ante. Etiam a hendrerit nisi. Curabitur faucibus bibendum nunc, non dictum urna varius at. Maecenas viverra ac turpis quis pharetra. Morbi sollicitudin eu neque eget hendrerit. Cras vel ante eu diam pharetra vestibulum.\n" +
            "\n" +
            "Vivamus hendrerit maximus metus vel semper. In pharetra lacus vel odio viverra, sed ultricies turpis interdum. Morbi aliquet, metus eget gravida semper, elit sem ullamcorper tellus, sit amet porta quam felis sed sem. Cras tristique euismod dignissim. Etiam eu ullamcorper orci, laoreet interdum lectus. Morbi blandit felis et magna mattis, non pulvinar est molestie. Suspendisse vehicula a mi eget tincidunt. Donec rhoncus lorem in enim tempor gravida. Suspendisse vel suscipit ipsum. Vivamus accumsan tortor leo, et placerat mi elementum eu. Suspendisse sodales nibh non sodales gravida. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.\n" +
            "\n" +
            "Ut a ligula hendrerit, fermentum tellus a, tristique sapien. Ut aliquet libero vel magna mattis, et accumsan urna pulvinar. Suspendisse molestie malesuada tempor. Nulla mollis condimentum fermentum. Cras sed sapien in odio iaculis consectetur. Nullam bibendum, ante at elementum ornare, leo purus fringilla lectus, quis blandit lectus leo non metus. Nullam porta nisi lorem, et semper nibh commodo eu. Nam eu nunc quis ex blandit dignissim. Etiam venenatis mollis orci, maximus placerat risus. Sed ut felis id nulla euismod commodo sed id mauris. Suspendisse potenti. ",
        deflatedBase64 =
          "eF61Vkuu3DYQ3M8p+gDC3CFwEsBAYjgwnD2H6jfTAUXK/Ajv+KnmT3rJLkA2nmeJn+rqqmrdbr+FyBvJnspGa3AhUpJMZuO8kA0+sc2cSySzyi7Jin8SO8l3+ok9G08HfrzJkmgzJeLnYEfG2rIlvN2dsRwNln/jlR7O+FVPt+QlCfng6Y3jxj6X7U5f2Tn8yelHYZz2LltJ5NkSwOCvhBNs2Lawhjt99hMnGSfYkelHMTjlkzMpkdkzjqJsrGShFKwEHIsqCOBDNIR/sAlLdo5aaHmIAaCUo1nqM/GW9xwSvQQADYfUinixXyNHXH6WfiglWlYGA1nR3OlLcc5s9IzmkNVgCV48igPNnrVALf7gl9jizP1206O34HQV0x45C1Y6DguAUCpgftcryrOcrwt4/RWvGEU9cQyFVbCBHTdKiQttAl4kLXq4A1TGf0AhoafoZ4c5eCW0C0y3dzgo4wH0gV/goiO4kneTJ/N3+iULapxVo62PF1qXEkic/Zn72rkXBsHBQKAXVGKwUZGib2VF6SXTWmTAXqaG3iKkKLqz7rrT74Yt2pFoNTuITrS/DHhCq/cQc9au3+lPOYyqqkKkvcSCMl2xqi9cdhS3l2wyk4H8awNtiKoGlaGTB8cwKEdhiWuLcHeIDyHTV9BfJWU8/RoNJ5WYrEqseCjMqd3u9J9dN6vE9aNQfl8oRI9qL60fYD/DUE8UMHu0qPcc4WGeJhvCWxp0Fc30JeEYkCRX5ZtoC/naN3XlZp7eoJGQwvBi9Tf2qCf1rpSBuVaGJ6jjU4nmIVqi5gV7afeAtJdJyAGAgp1UFHtIhSOgaRvN0BDX/qAoy6luh4WQCRB85g0rW287JktvptjKleIZO4VVCKi8GBi0SmIo+pBsuMFS95Bm2KX+c5e6GMLpm//AWrXBLHQBnFqMrlvFKp8ZySJr8bl3Agq6HFgdvtT1U7d9CUhuaAx6OpCaq51A+pXaWfUDYvCrRk/xdrmCKdANHSaKEn7V1yEHR5hHQ61RXq8ephqa/+BVsN+yrYpr4gKkaPpgAHJdtir0adAzG9HFYdEr212ldQzoMTAeTFmnwDykRZe+1RQc+Jcq0LPfvRYBjrjq0OnWbbJd+h0V/0judtlSDVgtf82F1t5L2DWVVtm/sQau3p/U85WEMypHEK/yRN8SFNw7CnquN4RoZZk5PIBDEhpaA/9MxXpldbaqZTMQUOpqKu4QhEQ11TA8RlpJO5gGAp7jCKJCAlQSpljv9HPQEIyv4JVo1xLMN5N003XK/nmqOwdYjbwzh+eHAlTehV7HzvhuqDgu4+zjySms6ps2dbTG8WDi+B8/Bm6377lGfp28U63LJTm793HG2faE0GZMve/nd0ufGsrTx7bpt80gqBp19PAjDfOz4UyR1o8xW/voR2GrdGiXz66qy6rSCk17Wh0kMFTfNubR/KoZgbI0R6NRZ5faKFpqaNUUvszqptqlZckQbXtY12sTqwXnTc1PdZxUyS198NahrH0fny4qji/NPppyPbff5y3/8tnly7Xx05w20mZKEK0r/duvDIdhsrXxN1w8UCiPsvZv4Y9t2oOqEAH9N3tPNKc=",
      )
  }
}
