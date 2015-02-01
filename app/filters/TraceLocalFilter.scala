package filters

import kamon.trace.TraceLocal
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{ Filter, RequestHeader, Result }

import scala.concurrent.Future

case class TraceLocalContainer(
  userId: Long,
  traceId: String,
  userAgent: String)

object TraceLocalKey extends TraceLocal.TraceLocalKey {
  type ValueType = TraceLocalContainer
}

object TraceLocalFilter extends Filter {
  val logger = Logger(this.getClass)
  val PersonIdKey = "X-Person-Id"
  val UserAgentKey = "User-Agent"

  // @see NginxのCustomLogFormatでロギングされているため、AccessLogに記録される。
  // trace_id["$sent_http_x_trace_id"]
  val ResponseIdKey = "X-Trace-Id"

  override def apply(next: (RequestHeader) => Future[Result])(header: RequestHeader): Future[Result] = {

    def onResult(result: Result) = {
      val traceLocalContainer = TraceLocal.retrieve(TraceLocalKey).getOrElse(TraceLocalContainer(0L, "unknown", "unknown"))
      result.withHeaders(ResponseIdKey -> traceLocalContainer.traceId)
    }

    val traceId: String = java.util.UUID.randomUUID.toString
    val personId: Long = header.headers.get(PersonIdKey).getOrElse("0").toLong
    val userAgent: String = header.headers.get(UserAgentKey).getOrElse("unknown")

    val traceLocalContainer: TraceLocalContainer = TraceLocalContainer(personId, traceId, userAgent)

    //update the TraceLocalStorage
    TraceLocal.store(TraceLocalKey)(traceLocalContainer)

    //call the action
    next(header).map(onResult)
  }
}
