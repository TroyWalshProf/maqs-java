/*
 * Copyright 2022 (C) Cognizant SoftVision, All rights Reserved
 */

package com.cognizantsoftvision.maqs.utilities.logging;

/**
 * Abstract logging interface base class.
 */
public abstract class Logger implements ILogger {

  /**
   * Default date format.
   */
  protected static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /**
   * Log Level value area.
   */
  private MessageType logLevel;

  /**
   *  Log Level value save area.
   */
  private MessageType logLevelSaved = MessageType.SUSPENDED;

  /**
   * Initializes a new instance of the Logger class.
   */
  protected Logger() {
    this(MessageType.INFORMATION);
  }

  /**
    * Initializes a new instance of the Logger class.
   *
   * @param level
   *          The logging level.
   */
  protected Logger(MessageType level) {
    this.logLevel = level;
  }

  /**
   * {@inheritdoc}
   */
  @Override
  public MessageType getLoggingLevel() {
    return this.logLevel;
  }

  /**
   * {@inheritdoc}
   */
  @Override
  public void setLoggingLevel(MessageType level) {
    this.logLevel = level;
  }

  /**
   * {@inheritdoc}
   */
  @Override
  public void suspendLogging() {
    if (this.logLevel != MessageType.SUSPENDED) {
      this.logLevelSaved = this.logLevel;
      this.logLevel = MessageType.SUSPENDED;
      this.logMessage(MessageType.VERBOSE, "Suspending Logging..");
    }
  }

  /**
   * {@inheritdoc}
   */
  @Override
  public void continueLogging() {
    // Check if the logging was suspended
    if (this.logLevelSaved != MessageType.SUSPENDED) {
      // Return to the log level at the suspension of logging
      this.logLevel = this.logLevelSaved;
    }

    this.logLevelSaved = MessageType.SUSPENDED;
    this.logMessage(MessageType.VERBOSE, "Logging Continued..");
  }

  /**
   * Determine if the message should be logged.
   * The message should be logged if it's level is greater than or equal to the current logging level.
   *
   * @param messageType The type of message being logged.
   * @return True if the message should be logged.
   */
  protected boolean shouldMessageBeLogged(MessageType messageType) {
    // The message should be logged if it's level is less than or equal to the current logging level
    return messageType.getValue() <= this.logLevel.getValue();
  }
}
