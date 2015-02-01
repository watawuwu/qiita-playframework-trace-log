import filters.TraceLocalFilter
import play.api._
import play.api.mvc.{ RequestHeader, _ }

object Global extends GlobalSettings {
  override def doFilter(a: EssentialAction): EssentialAction = {
    Filters(super.doFilter(a), TraceLocalFilter)
  }
}
