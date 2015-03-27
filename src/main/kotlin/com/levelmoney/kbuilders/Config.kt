package com.levelmoney.kbuilders

import joptsimple.OptionSet
import kotlin.properties.Delegates

/**
 * Created by Aaron Sarazan on 3/27/15
 * Copyright(c) 2015 Level, Inc.
 */
public data class Config
(val inline: Boolean,
 val methodPrefix: String)