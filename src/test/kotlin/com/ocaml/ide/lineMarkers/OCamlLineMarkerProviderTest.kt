package com.ocaml.ide.lineMarkers

import com.ocaml.icons.OCamlIcons
import com.ocaml.ide.OCamlBasePlatformTestCase
import org.junit.Test

class OCamlLineMarkerProviderTest : OCamlBasePlatformTestCase() {

    @Test
    fun testOneMarker() {
        val ml = myFixture.configureByText("A.ml", "let x = ()")
        val mli = myFixture.configureByText("A.mli", "val x : unit")
        val mlMarkers = OCamlLineMarkerProvider().collectNavigationMarkersForTest(ml)
        val mliMarkers = OCamlLineMarkerProvider().collectNavigationMarkersForTest(mli)
        assertSize(1, mlMarkers)
        assertSize(1, mliMarkers)
        assertEquals(OCamlIcons.Gutter.IMPLEMENTING, mlMarkers.first().icon)
        assertEquals(OCamlIcons.Gutter.IMPLEMENTED, mliMarkers.first().icon)
    }

    @Test
    fun testMultipleMarkers() {
        val ml = myFixture.configureByText("A.ml", "let x = ();; let x = ();; let y = ()")
        val mli = myFixture.configureByText("A.mli", "val x : unit;;val y: unit")
        val mlMarkers = OCamlLineMarkerProvider().collectNavigationMarkersForTest(ml)
        val mliMarkers = OCamlLineMarkerProvider().collectNavigationMarkersForTest(mli)
        assertSize(3, mlMarkers)
        assertSize(2, mliMarkers)
        mlMarkers.forEach { assertEquals(OCamlIcons.Gutter.IMPLEMENTING, it.icon) }
        mliMarkers.forEach { assertEquals(OCamlIcons.Gutter.IMPLEMENTED, it.icon) }
    }
}