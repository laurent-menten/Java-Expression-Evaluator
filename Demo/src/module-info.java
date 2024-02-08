module Demo
{
	requires lib.lmenten.math;

	requires java.desktop;
	requires java.logging;

	exports be.lmenten.math.demo.calc to lib.lmenten.math;
	exports be.lmenten.math.demo.dnd to lib.lmenten.math;
	exports be.lmenten.math.demo.simple to lib.lmenten.math;
}
