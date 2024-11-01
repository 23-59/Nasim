package com.golden_minute.nasim.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Response(

	@SerialName("Response")
	val response: List<ResponseItem> = emptyList()
)

@Serializable
data class LocalNames(

	@SerialName("hi")
	val hi: String,

	@SerialName("ps")
	val ps: String,

	@SerialName("pt")
	val pt: String,

	@SerialName("hr")
	val hr: String,

	@SerialName("ht")
	val ht: String,

	@SerialName("hu")
	val hu: String,

	@SerialName("yi")
	val yi: String,

	@SerialName("hy")
	val hy: String,

	@SerialName("yo")
	val yo: String,

	@SerialName("ia")
	val ia: String,

	@SerialName("id")
	val id: String,

	@SerialName("ie")
	val ie: String,

	@SerialName("feature_name")
	val featureName: String,

	@SerialName("af")
	val af: String,

	@SerialName("io")
	val io: String,

	@SerialName("is")
	val unusedIs: String,

	@SerialName("it")
	val it: String,

	@SerialName("am")
	val am: String,

	@SerialName("an")
	val an: String,

	@SerialName("zh")
	val zh: String,

	@SerialName("ar")
	val ar: String,

	@SerialName("ja")
	val ja: String,

	@SerialName("az")
	val az: String,

	@SerialName("rm")
	val rm: String,

	@SerialName("ro")
	val ro: String,

	@SerialName("ba")
	val ba: String,

	@SerialName("be")
	val be: String,

	@SerialName("ru")
	val ru: String,

	@SerialName("bg")
	val bg: String,

	@SerialName("bn")
	val bn: String,

	@SerialName("bo")
	val bo: String,

	@SerialName("sc")
	val sc: String,

	@SerialName("br")
	val br: String,

	@SerialName("bs")
	val bs: String,

	@SerialName("se")
	val se: String,

	@SerialName("ka")
	val ka: String,

	@SerialName("sk")
	val sk: String,

	@SerialName("sl")
	val sl: String,

	@SerialName("so")
	val so: String,

	@SerialName("sq")
	val sq: String,

	@SerialName("ca")
	val ca: String,

	@SerialName("sr")
	val sr: String,

	@SerialName("kk")
	val kk: String,

	@SerialName("kl")
	val kl: String,

	@SerialName("sv")
	val sv: String,

	@SerialName("kn")
	val kn: String,

	@SerialName("sw")
	val sw: String,

	@SerialName("ko")
	val ko: String,

	@SerialName("ku")
	val ku: String,

	@SerialName("kw")
	val kw: String,

	@SerialName("ta")
	val ta: String,

	@SerialName("cs")
	val cs: String,

	@SerialName("te")
	val te: String,

	@SerialName("tg")
	val tg: String,

	@SerialName("cv")
	val cv: String,

	@SerialName("th")
	val th: String,

	@SerialName("la")
	val la: String,

	@SerialName("cy")
	val cy: String,

	@SerialName("lb")
	val lb: String,

	@SerialName("tk")
	val tk: String,

	@SerialName("tl")
	val tl: String,

	@SerialName("da")
	val da: String,

	@SerialName("tr")
	val tr: String,

	@SerialName("tt")
	val tt: String,

	@SerialName("de")
	val de: String,

	@SerialName("lt")
	val lt: String,

	@SerialName("lv")
	val lv: String,

	@SerialName("ug")
	val ug: String,

	@SerialName("uk")
	val uk: String,

	@SerialName("mi")
	val mi: String,

	@SerialName("ur")
	val ur: String,

	@SerialName("mk")
	val mk: String,

	@SerialName("ml")
	val ml: String,

	@SerialName("mn")
	val mn: String,

	@SerialName("uz")
	val uz: String,

	@SerialName("mr")
	val mr: String,

	@SerialName("ms")
	val ms: String,

	@SerialName("el")
	val el: String,

	@SerialName("en")
	val en: String,

	@SerialName("eo")
	val eo: String,

	@SerialName("my")
	val my: String,

	@SerialName("es")
	val es: String,

	@SerialName("et")
	val et: String,

	@SerialName("eu")
	val eu: String,

	@SerialName("vi")
	val vi: String,

	@SerialName("vo")
	val vo: String,

	@SerialName("fa")
	val fa: String,

	@SerialName("nl")
	val nl: String,

	@SerialName("nn")
	val nn: String,

	@SerialName("no")
	val no: String,

	@SerialName("fi")
	val fi: String,

	@SerialName("fo")
	val fo: String,

	@SerialName("fr")
	val fr: String,

	@SerialName("fy")
	val fy: String,

	@SerialName("oc")
	val oc: String,

	@SerialName("ga")
	val ga: String,

	@SerialName("gd")
	val gd: String,

	@SerialName("ascii")
	val ascii: String,

	@SerialName("or")
	val or: String,

	@SerialName("os")
	val os: String,

	@SerialName("gl")
	val gl: String,

	@SerialName("pa")
	val pa: String,

	@SerialName("pl")
	val pl: String,

	@SerialName("he")
	val he: String
)

@Serializable
data class ResponseItem(

	@SerialName("local_names")
	val localNames: LocalNames,

	@SerialName("country")
	val country: String,

	@SerialName("name")
	val name: String,

	@SerialName("lon")
	val lon: Float,

	@SerialName("lat")
	val lat: Float
)
