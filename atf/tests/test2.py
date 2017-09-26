import random

try:
    import unittest2 as unittest
except ImportError:
    import unittest

class SimpleTest(unittest.TestCase):
    @unittest.skip("demonstrating skipping")
    def test_skipped(self):
        self.fail("shouldn't happen")

    def test_skipped_now(self):
        self.fail("shouldn't happen")

    def test_pass(self):
        self.assertEqual(10, 7 + 3)

    def test_pass_again(self):
        self.assertEqual(15, 10 + 5)

    def test_pass_and_again(self):
        self.assertEqual(20, 10 + 10)


    # def test_fail(self):
    #     self.assertEqual(11, 7 + 3)