package na.datapipe.source.engine.twitter

import akka.actor.Props
import na.datapipe.source.engine.ElementLoader
import na.datapipe.transformer.model.TransformTweet
/**
 * @author nader albert
 * @since  4/08/2015.
 */
class TweetLoader(transformersHost: String, transformersPort: String) extends ElementLoader {
  override val host = transformersHost
  override val port = transformersPort

  override val transformCommand = (tweet :String, id :Int) => TransformTweet(tweet, id)
}

object TweetLoader {
  def props(transformersHost: String, transformersPort: String) = Props(classOf[TweetLoader],transformersHost,transformersPort)
}