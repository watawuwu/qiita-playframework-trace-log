package controllers

import play.api._
import play.api.mvc._
import play.api.Logger


object Application extends Controller {

  def index = Action {
    Logger.info("info log")
    Ok("OK")
  }

}
